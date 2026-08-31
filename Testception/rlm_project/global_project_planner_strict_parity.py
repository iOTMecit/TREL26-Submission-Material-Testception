#!/usr/bin/env python3
"""
Testception Single Global Context Baseline v2.

One project-wide LLM request receives the SAME Crawljax result.json, ALL saved
DOM states, ALL recorded transitions, and the SAME application hints used by
recursive Testception. The generated plan is graph-validated before it is
passed to the same DANTE-compatible Java generator.

No deterministic scenario fallback is used. The harness may materialize only
the initial recorded navigation prefix from the real application start state to
the first state selected by the global LLM. This is test bootstrap, not plan
repair: after the first selected state, later state jumps are never repaired.
If the LLM chooses a DOM-only action whose target is unknown, the executable
prefix is preserved as a leaf scenario, matching recursive DOM_FALLBACK
semantics.

If the global request exceeds context limits or returns unusable output, that
failure is an experimental result and is recorded in
global-baseline-results/<app>/.
"""

from __future__ import annotations

import ast
import json
import os
import re
import sys
import time
from collections import Counter, defaultdict, deque
from pathlib import Path
from typing import Any

import instructor
from litellm import completion
from pydantic import BaseModel, Field

try:
    from litellm import token_counter
except Exception:
    token_counter = None

from crawljax_parser import extract_actionable_skeleton
from dante_suite_generator import generate_dante_suites
from worker_agent import fallback_input_value


class GlobalAction(BaseModel):
    state_name: str
    selected_id: str
    robust_xpath: str = ""
    action: str = Field(description="click, input, or select")
    input_value: str = ""


class GlobalScenario(BaseModel):
    scenario_name: str
    scenario_reason: str
    start_state: str
    actions: list[GlobalAction]


class GlobalPlan(BaseModel):
    scenario_reason: str
    scenarios: list[GlobalScenario]


CLIENT = instructor.from_litellm(completion)


def nx(v: Any) -> str:
    return str(v or "").replace(" ", "").replace("xpath", "").strip().lower()


def nt(v: Any) -> str:
    return re.sub(r"\s+", " ", str(v or "").strip().lower())


def state_name(v: Any) -> str:
    raw = str(v or "").strip()
    if not raw:
        return ""
    return raw if raw.endswith(".html") else raw + ".html"


def xpath_literal(value: str) -> str:
    if "'" not in value:
        return "'" + value + "'"
    if '"' not in value:
        return '"' + value + '"'
    return "concat(" + ", \"'\", ".join("'" + p + "'" for p in value.split("'")) + ")"


def load_project_configs(mentor: Path) -> dict:
    tree = ast.parse(mentor.read_text(encoding="utf-8"), filename=str(mentor))
    for node in tree.body:
        if isinstance(node, ast.Assign):
            for target in node.targets:
                if isinstance(target, ast.Name) and target.id == "PROJECT_CONFIGS":
                    return ast.literal_eval(node.value)
    raise RuntimeError("PROJECT_CONFIGS not found in mentor_orchestrator.py")


def resolve_crawl_dir(repo: Path, app: str, cfg: dict) -> Path:
    root = repo / "dante" / "applications" / app / "localhost" / "crawl-with-inputs"
    if not root.is_dir():
        raise FileNotFoundError(root)
    crawl0 = root / "crawl0"
    if cfg.get("has_crawl0") and (crawl0 / "result.json").is_file() and (crawl0 / "doms").is_dir():
        return crawl0
    return root


def ignored(el: dict, cfg: dict) -> bool:
    blob = " ".join(nt(el.get(k)) for k in (
        "text", "placeholder", "label_text", "title_attr",
        "aria_label", "href", "class_attr"
    ))
    xpblob = " ".join(str(el.get(k, "") or "") for k in (
        "xpath", "absolute_xpath", "robust_xpath"
    )).lower()

    for item in cfg.get("ignore_texts", []) or []:
        if nt(item) and nt(item) in blob:
            return True
    for item in cfg.get("ignore_href_contains", []) or []:
        if nt(item) and nt(item) in blob:
            return True
    for item in cfg.get("ignore_xpaths", []) or []:
        low = str(item or "").lower()
        if low and low in xpblob:
            return True
        m = re.search(
            r"contains\s*\(\s*normalize-space\(\.\)\s*,\s*['\"]([^'\"]+)['\"]\s*\)",
            low,
        )
        if m and nt(m.group(1)) in blob:
            return True
    return False


def choose_replay_value(xpath: str, raw: Any, index: int | None = None) -> str:
    value = str(raw or "").strip()
    if not value:
        return ""
    if "/SELECT" in xpath.upper():
        opts = [x.strip() for x in value.split(",") if x.strip()]
        for preferred in ("Euro (EUR)", "United States dollar (USD)", "Pound sterling (GBP)"):
            if preferred in opts:
                return preferred
        return opts[0] if opts else value
    return {
        1: "QA Test Event",
        2: "Alice",
        3: "Bob",
        4: "Charlie",
        5: "Delta",
    }.get(index, "QA Test Value")


def replay_value(cfg: dict, kind: str, ident: str, xp: str, raw: Any, index: int) -> str:
    value = str(raw or "").strip()
    if not value:
        return ""
    key = nt(ident)
    if cfg.get("preserve_edge_input_values", False):
        override = (cfg.get("input_overrides", {}) or {}).get(key)
        if override is not None:
            return str(override)
        if key == "telephone":
            digits = re.sub(r"\D", "", value)
            return digits or "5551234567"
        return value
    return choose_replay_value(xp, value, index)


def parse_input_values(raw: Any, cfg: dict) -> list[dict]:
    text = str(raw or "").strip()
    if not text or text.lower() == "none":
        return []
    pattern = re.compile(
        r"(?:^|:)(xpath|id|name)##(.*?)##(.*?)(?=:(?:xpath|id|name)##|$)",
        re.I,
    )
    out = []
    idx = 0
    for m in pattern.finditer(text):
        kind = nt(m.group(1))
        ident = str(m.group(2) or "").strip()
        raw_value = str(m.group(3) or "").strip()
        if not ident or not raw_value:
            continue
        if kind == "xpath":
            xp = ident
        elif kind == "id":
            xp = "//*[@id = " + xpath_literal(ident) + "]"
        else:
            xp = "//*[@name = " + xpath_literal(ident) + "]"
        idx += 1
        value = replay_value(cfg, kind, ident, xp, raw_value, idx)
        if value:
            out.append({
                "xpath": xp,
                "action": "input",
                "input_value": value,
                "identification_type": kind,
                "identifier": ident,
            })
    return out


def collect_states(crawl: Path, cfg: dict):
    doms = crawl / "doms"
    files = sorted(doms.glob("*.html"))
    if not files:
        raise RuntimeError("No DOM states found in " + str(doms))

    all_elements = []
    states = []
    elements_by_state = {}

    for si, f in enumerate(files):
        elems = [
            e for e in extract_actionable_skeleton(str(f))
            if not ignored(e, cfg)
        ]
        enriched = []
        for e in elems:
            x = dict(e)
            x["global_id"] = f"g{si}_{e.get('llm_id', '')}"
            x["state_name"] = f.name
            all_elements.append(x)
            enriched.append(x)
        elements_by_state[f.name] = enriched
        states.append({"state_name": f.name, "elements": enriched})
    return all_elements, states, elements_by_state


def load_edges(crawl: Path, cfg: dict, available: set[str]):
    data = json.loads((crawl / "result.json").read_text(encoding="utf-8"))
    edges = []
    by_state = defaultdict(list)
    for i, raw in enumerate(data.get("edges", [])):
        if not isinstance(raw, dict):
            continue
        src = state_name(raw.get("from"))
        dst = state_name(raw.get("to"))
        xp = str(raw.get("id", "") or "").strip()
        if not src or not xp:
            continue
        edge = {
            "edge_index": i,
            "from": src,
            "to": dst,
            "xpath": xp,
            "xpath_key": nx(xp),
            "raw_text": str(raw.get("text", "") or ""),
            "text": nt(raw.get("text")),
            "target_dom_available": dst in available,
            "input_steps": parse_input_values(raw.get("inputValues"), cfg),
        }
        edges.append(edge)
        by_state[src].append(edge)
    return edges, dict(by_state)


def choose_start(available: set[str], edges: list[dict]) -> str:
    for candidate in ("index.html", "state0.html"):
        if candidate in available:
            return candidate
    for e in edges:
        if e["from"] in available:
            return e["from"]
    return sorted(available)[0]


def form_summary(elements: list[dict]) -> dict:
    fields, commits = [], []
    for e in elements:
        tag = nt(e.get("tag"))
        visible = nt(" ".join(str(e.get(k, "") or "") for k in (
            "text", "label_text", "placeholder", "title_attr", "aria_label"
        )))
        if tag in {"input", "textarea", "select", "mat-select"}:
            fields.append({
                "global_id": e["global_id"],
                "tag": tag,
                "type": e.get("type_attr", ""),
                "label": e.get("label_text", ""),
                "placeholder": e.get("placeholder", ""),
                "options": (e.get("options") or [])[:8],
            })
        if tag in {"button", "a", "input"} and any(
            k in visible for k in ("save", "create", "submit", "add", "update", "register", "login")
        ):
            commits.append({
                "global_id": e["global_id"],
                "tag": tag,
                "text": e.get("text", ""),
            })
    return {"fields": fields, "submit_like_controls": commits}


def build_prompt(app: str, start: str, states: list[dict], edge_map: dict, hints: list[str], cap: int) -> str:
    lines = [
        "GLOBAL_PROJECT_CONTEXT",
        f"APPLICATION: {app}",
        f"START_STATE: {start}",
        "",
        "You are the SINGLE global planner baseline.",
        "You see ALL saved states, ALL DOM actions and ALL recorded transitions at once.",
        "Create the complete project-level test suite in ONE response.",
        "",
        "STRICT GRAPH RULES:",
        "- Every scenario MUST declare start_state=START_STATE.",
        (
            "- You MAY choose the first semantic action from any saved state "
            "that is reachable from START_STATE. The harness materializes ONLY "
            "that initial recorded navigation prefix."
        ),
        (
            "- After the first selected state, NEVER teleport between states. "
            "Every later state change must be caused by your own preceding "
            "recorded click."
        ),
        "- input/select keep the current state.",
        (
            "- A DOM-only click with no recorded transition is allowed. Its "
            "target is unknown to the crawl graph, so the executable scenario "
            "will terminate at that click."
        ),
        (
            "- Later invalid state jumps are NOT repaired. The already valid "
            "executable prefix is kept as a truncated scenario."
        ),
        "- Use only provided GLOBAL_ID values.",
        "- Prefer meaningful business CRUD/form/search/filter flows over static links.",
        "- For happy-path forms, fill required-looking fields before the final commit.",
        "- One negative validation flow is useful; do not repeat it endlessly.",
        "",
    ]
    if cap > 0:
        lines.append(f"SCENARIO_BUDGET: at most {cap} scenarios.")
    else:
        lines.append(
            "SCENARIO_BUDGET: no artificial fixed count; return as many distinct high-value executable scenarios as needed."
        )
    if hints:
        lines += ["", "APP_HINTS:"] + ["- " + h for h in hints]

    lines += ["", "FULL_GRAPH_AND_DOM_CONTEXT", "=========================="]

    for st in states:
        name = st["state_name"]
        elems = st["elements"]
        outgoing = edge_map.get(name, [])
        lines += [
            "",
            f"=== STATE {name} ===",
            "FORM_SUMMARY: " + json.dumps(form_summary(elems), ensure_ascii=False, separators=(",", ":")),
            "RECORDED_TRANSITIONS:",
        ]
        if not outgoing:
            lines.append("- NONE")
        for edge in outgoing:
            lines.append("- " + json.dumps({
                "edge_index": edge["edge_index"],
                "text": edge["raw_text"],
                "click_xpath": edge["xpath"],
                "target_state": edge["to"],
                "target_dom_available": edge["target_dom_available"],
                "input_values": edge["input_steps"],
            }, ensure_ascii=False, separators=(",", ":")))

        lines.append("ACTIONABLE_DOM:")
        for e in elems:
            xp = e.get("xpath") or e.get("absolute_xpath") or ""
            targets = [
                ed["to"] for ed in outgoing
                if nx(ed["xpath"]) == nx(xp)
            ]
            payload = {
                "GLOBAL_ID": e["global_id"],
                "tag": e.get("tag", ""),
                "type": e.get("type_attr", ""),
                "text": e.get("text", ""),
                "label": e.get("label_text", ""),
                "placeholder": e.get("placeholder", ""),
                "id": e.get("id_attr", ""),
                "name": e.get("name_attr", ""),
                "aria": e.get("aria_label", ""),
                "xpath": xp,
                "recorded_targets": targets,
            }
            if nt(e.get("tag")) in {"select", "mat-select"}:
                payload["options"] = e.get("options", [])
            lines.append("- " + json.dumps(payload, ensure_ascii=False, separators=(",", ":")))
    return "\n".join(lines) + "\n"


def count_tokens(model: str, messages: list[dict]):
    if token_counter is None:
        return None
    try:
        return int(token_counter(model=model, messages=messages))
    except Exception:
        return None


def one_global_call(model: str, key: str, prompt: str, max_tokens: int):
    system = (
        "ROLE: Senior QA Automation Engineer acting as a SINGLE GLOBAL PROJECT PLANNER. "
        "Plan executable scenarios from the complete Crawljax graph and DOM context. "
        "Every scenario must preserve graph continuity. Do not invent elements or hidden transitions."
    )
    messages = [
        {"role": "system", "content": system},
        {"role": "user", "content": prompt},
    ]
    started = time.perf_counter()
    result = CLIENT.chat.completions.create(
        model=model,
        response_model=GlobalPlan,
        messages=messages,
        api_key=key,
        temperature=0.2,
        max_tokens=max_tokens,
        max_retries=0,
    )
    return result, {
        "llm_calls_attempted": 1,
        "llm_calls_completed": 1,
        "elapsed_seconds": time.perf_counter() - started,
        "input_tokens": count_tokens(model, messages),
    }


def find_edge(current: str, el: dict, edge_map: dict):
    xp = nx(el.get("xpath") or el.get("absolute_xpath"))
    if xp:
        for edge in edge_map.get(current, []):
            if edge["xpath_key"] == xp:
                return edge
    text = nt(el.get("text") or el.get("label_text") or el.get("aria_label"))
    if text:
        for edge in edge_map.get(current, []):
            et = edge["text"]
            if et and (text == et or text in et or et in text):
                return edge
    return None



def clean_edge_xpath(value: Any) -> str:
    return re.sub(r"^\s*xpath\s+", "", str(value or "").strip(), flags=re.I)


def shortest_entry_prefix(start: str, target: str, edge_map: dict):
    """
    Find the shortest recorded path only for bootstrap from the real app start
    state to the first state selected by the global LLM.
    """
    if start == target:
        return []

    queue = deque([(start, [])])
    visited = {start}

    while queue:
        current, path = queue.popleft()
        for edge in edge_map.get(current, []):
            if not edge.get("target_dom_available"):
                continue

            nxt = edge.get("to", "")
            if not nxt or nxt in visited:
                continue

            new_path = path + [edge]
            if nxt == target:
                return new_path

            visited.add(nxt)
            queue.append((nxt, new_path))

    return None


def materialize_recorded_edge(
    edge: dict,
    elements_by_state: dict,
    scenario_reason: str,
    cfg: dict,
    bucket: str = "global_entry_prefix",
):
    steps = []

    if cfg.get("use_edge_input_replay", True):
        steps.extend(
            edge_replay_steps(
                edge,
                edge.get("from", ""),
                elements_by_state,
                scenario_reason,
            )
        )

    xp = clean_edge_xpath(edge.get("xpath", ""))
    steps.append(
        {
            "selected_id": f"recorded_edge_{edge.get('edge_index', '')}",
            "element": edge.get("raw_text") or f"Recorded edge {edge.get('edge_index', '')}",
            "xpath": xp,
            "action": "click",
            "input_value": "",
            "options": [],
            "tag": "",
            "type_attr": "",
            "feature_bucket": bucket,
            "feature_signature": (
                f"{bucket}:click:{nx(xp)}:"
                f"{nt(edge.get('raw_text'))[:30]}"
            ),
            "state": edge.get("from", ""),
            "state_type": edge.get("from", ""),
            "scenario_reason": scenario_reason,
            "recorded_edge_materialized": True,
            "recorded_edge_index": edge.get("edge_index"),
            "recorded_target_state": edge.get("to", ""),
        }
    )

    return steps


def replay_element(elements: list[dict], replay: dict):
    kind = nt(replay.get("identification_type"))
    ident = str(replay.get("identifier", "") or "")
    if kind == "id":
        for e in elements:
            if str(e.get("id_attr", "") or "") == ident:
                return e
    if kind == "name":
        for e in elements:
            if str(e.get("name_attr", "") or "") == ident:
                return e
    target = nx(replay.get("xpath"))
    for e in elements:
        if target in {nx(e.get("xpath")), nx(e.get("absolute_xpath"))}:
            return e
    return None


def edge_replay_steps(edge: dict, current: str, elements_by_state: dict, reason: str):
    out = []
    for replay in edge.get("input_steps", []):
        e = replay_element(elements_by_state.get(current, []), replay)
        if e is None:
            continue
        tag = nt(e.get("tag"))
        action = "select" if tag in {"select", "mat-select"} else "input"
        xp = e.get("xpath") or e.get("absolute_xpath") or replay.get("xpath") or ""
        out.append({
            "selected_id": e.get("llm_id", ""),
            "element": e.get("text") or e.get("placeholder") or e.get("label_text") or e.get("id_attr") or "result.json inputValues replay",
            "xpath": xp,
            "action": action,
            "input_value": replay.get("input_value", ""),
            "options": e.get("options", []),
            "tag": e.get("tag", ""),
            "type_attr": e.get("type_attr", ""),
            "feature_bucket": "result_json_input_replay",
            "feature_signature": f"edge_replay:{action}:{nx(xp)}:{nt(replay.get('input_value'))[:30]}",
            "state": current,
            "state_type": current,
            "scenario_reason": reason,
            "edge_replay": True,
        })
    return out


def merge_replay(path: list[dict], steps: list[dict], preserve: bool):
    for replay in steps:
        key = (replay["state"], replay["action"], nx(replay["xpath"]))
        existing = next((
            s for s in path
            if (s.get("state", ""), s.get("action", ""), nx(s.get("xpath"))) == key
        ), None)
        if existing:
            if preserve and replay["action"] in {"input", "select"}:
                existing["input_value"] = replay["input_value"]
                existing["feature_signature"] = replay["feature_signature"]
        else:
            path.append(replay)


def convert(action: GlobalAction, e: dict, current: str, scenario: GlobalScenario):
    tag = nt(e.get("tag"))
    typ = nt(e.get("type_attr"))
    value = str(action.input_value or "")
    if tag == "input" and typ in {"submit", "button", "reset"}:
        kind, value = "click", ""
    elif tag in {"input", "textarea"}:
        kind = "input"
        if not value.strip():
            value = fallback_input_value(e)
    elif tag in {"select", "mat-select"}:
        kind = "select"
        if not value.strip():
            value = fallback_input_value(e)
    else:
        kind, value = "click", ""

    xp = e.get("xpath") or e.get("absolute_xpath") or action.robust_xpath
    return {
        "selected_id": e.get("llm_id", ""),
        "element": e.get("text") or e.get("placeholder") or e.get("label_text") or scenario.scenario_name,
        "xpath": xp,
        "action": kind,
        "input_value": value,
        "options": e.get("options", []),
        "tag": e.get("tag", ""),
        "type_attr": e.get("type_attr", ""),
        "feature_bucket": "global_one_shot",
        "feature_signature": f"global:{kind}:{nx(xp)}:{nt(value)[:30]}",
        "state": current,
        "state_type": current,
        "scenario_reason": scenario.scenario_reason,
    }


def validate(
    plan: GlobalPlan,
    start: str,
    global_map: dict,
    elements_by_state: dict,
    edge_map: dict,
    cfg: dict,
    cap: int,
):
    valid = []
    reports = []
    rejection_reasons = Counter()
    truncation_reasons = Counter()
    scenarios = plan.scenarios[:cap] if cap > 0 else plan.scenarios

    total_entry_edges = 0
    complete_valid_count = 0
    truncated_valid_count = 0

    for idx, scenario in enumerate(scenarios):
        declared_start = state_name(scenario.start_state)
        report = {
            "scenario_index": idx,
            "scenario_name": scenario.scenario_name,
            "declared_start_state": declared_start,
            "valid": False,
            "truncated": False,
            "reason": "",
            "validated_actions": 0,
            "llm_actions_executed": 0,
            "entry_prefix_edges_inserted": 0,
            "entry_state": "",
            "final_known_state": start,
        }

        if declared_start != start:
            report["reason"] = "INVALID_START_STATE"
            reports.append(report)
            rejection_reasons[report["reason"]] += 1
            continue

        if not scenario.actions:
            report["reason"] = "EMPTY_SCENARIO"
            reports.append(report)
            rejection_reasons[report["reason"]] += 1
            continue

        first_action = scenario.actions[0]
        first_element = global_map.get(first_action.selected_id)
        if first_element is None:
            report["reason"] = "UNKNOWN_FIRST_GLOBAL_ID"
            reports.append(report)
            rejection_reasons[report["reason"]] += 1
            continue

        first_declared_state = state_name(first_action.state_name)
        first_actual_state = state_name(first_element.get("state_name"))

        if first_declared_state != first_actual_state:
            report["reason"] = "FIRST_ACTION_STATE_ID_MISMATCH"
            reports.append(report)
            rejection_reasons[report["reason"]] += 1
            continue

        entry_prefix = shortest_entry_prefix(
            start,
            first_actual_state,
            edge_map,
        )

        if entry_prefix is None:
            report["reason"] = "UNREACHABLE_ENTRY_STATE"
            report["entry_state"] = first_actual_state
            reports.append(report)
            rejection_reasons[report["reason"]] += 1
            continue

        path = []
        current = start

        for edge in entry_prefix:
            path.extend(
                materialize_recorded_edge(
                    edge,
                    elements_by_state,
                    scenario.scenario_reason,
                    cfg,
                )
            )
            current = edge["to"]

        report["entry_prefix_edges_inserted"] = len(entry_prefix)
        report["entry_state"] = first_actual_state
        total_entry_edges += len(entry_prefix)

        if current != first_actual_state:
            report["reason"] = "ENTRY_PREFIX_INTERNAL_ERROR"
            reports.append(report)
            rejection_reasons[report["reason"]] += 1
            continue

        truncate_reason = ""
        hard_reject_reason = ""
        llm_actions_executed = 0

        for ai, action in enumerate(scenario.actions):
            element = global_map.get(action.selected_id)
            if element is None:
                if llm_actions_executed > 0:
                    truncate_reason = "UNKNOWN_GLOBAL_ID"
                else:
                    hard_reject_reason = "UNKNOWN_GLOBAL_ID"
                break

            declared = state_name(action.state_name)
            actual = state_name(element.get("state_name"))

            if declared != actual:
                if llm_actions_executed > 0:
                    truncate_reason = "ACTION_STATE_ID_MISMATCH"
                else:
                    hard_reject_reason = "ACTION_STATE_ID_MISMATCH"
                break

            if actual != current:
                # Deliberately do not repair later state jumps.
                if llm_actions_executed > 0:
                    truncate_reason = "INVALID_STATE_JUMP"
                else:
                    hard_reject_reason = "INVALID_STATE_JUMP"
                break

            step = convert(action, element, current, scenario)
            if not step["xpath"]:
                if llm_actions_executed > 0:
                    truncate_reason = "EMPTY_XPATH"
                else:
                    hard_reject_reason = "EMPTY_XPATH"
                break

            if step["action"] in {"input", "select"}:
                path.append(step)
                llm_actions_executed += 1
                continue

            edge = find_edge(current, element, edge_map)

            if edge is None:
                # Match recursive DOM_FALLBACK semantics: execute the known DOM
                # click, but terminate because its target state is unknown.
                path.append(step)
                llm_actions_executed += 1
                if ai != len(scenario.actions) - 1:
                    truncate_reason = "DOM_ONLY_CLICK_UNKNOWN_TARGET"
                break

            if cfg.get("use_edge_input_replay", True):
                merge_replay(
                    path,
                    edge_replay_steps(
                        edge,
                        current,
                        elements_by_state,
                        scenario.scenario_reason,
                    ),
                    bool(cfg.get("preserve_edge_input_values", False)),
                )

            path.append(step)
            llm_actions_executed += 1

            if edge["target_dom_available"]:
                current = edge["to"]
            else:
                if ai != len(scenario.actions) - 1:
                    truncate_reason = "MISSING_TARGET_DOM"
                break

        report["validated_actions"] = len(path)
        report["llm_actions_executed"] = llm_actions_executed
        report["final_known_state"] = current

        if hard_reject_reason:
            report["reason"] = hard_reject_reason
            reports.append(report)
            rejection_reasons[hard_reject_reason] += 1
            continue

        # Do not accept navigation bootstrap alone as an LLM-generated scenario.
        if llm_actions_executed == 0:
            report["reason"] = "NO_EXECUTABLE_LLM_ACTION"
            reports.append(report)
            rejection_reasons[report["reason"]] += 1
            continue

        report["valid"] = True

        if truncate_reason:
            report["truncated"] = True
            report["reason"] = "TRUNCATED_" + truncate_reason
            truncation_reasons[truncate_reason] += 1
            truncated_valid_count += 1
        else:
            report["reason"] = "VALID"
            complete_valid_count += 1

        reports.append(report)
        valid.append(path)

    return valid, {
        "raw_scenario_count": len(plan.scenarios),
        "budgeted_scenario_count": len(scenarios),
        "valid_scenario_count": len(valid),
        "complete_valid_scenario_count": complete_valid_count,
        "truncated_valid_scenario_count": truncated_valid_count,
        "rejected_scenario_count": len(scenarios) - len(valid),
        "rejection_reasons": dict(rejection_reasons),
        "truncation_reasons": dict(truncation_reasons),
        "entry_prefix_edges_inserted_total": total_entry_edges,
        "scenarios": reports,
    }


def dump(path: Path, obj: Any):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(obj, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def count_java_tests(path: Path) -> int:
    if not path.is_file():
        return 0
    return len(re.findall(r"\bpublic\s+void\s+test\d{3}\s*\(", path.read_text(encoding="utf-8")))


def fail_category(exc: Exception) -> str:
    msg = nt(exc)
    if any(x in msg for x in ("context length", "maximum context", "context window", "too many tokens", "prompt is too long")):
        return "CONTEXT_LIMIT"
    if any(x in msg for x in ("validation", "pydantic", "json", "response_model")):
        return "STRUCTURED_OUTPUT_FAILURE"
    if "timeout" in msg:
        return "TIMEOUT"
    return "LLM_REQUEST_FAILURE"


def main() -> int:
    if len(sys.argv) < 2 or len(sys.argv) > 3:
        print(
            "Usage: python3 global_project_planner_strict_parity.py "
            "<application> [--reuse-plan]"
        )
        return 2

    app = sys.argv[1].lower()
    reuse_plan = len(sys.argv) == 3 and sys.argv[2] == "--reuse-plan"

    if len(sys.argv) == 3 and not reuse_plan:
        print("ERROR: unknown option:", sys.argv[2])
        return 2
    rlm_dir = Path(__file__).resolve().parent
    repo = rlm_dir.parents[1]
    cfgs = load_project_configs(rlm_dir / "mentor_orchestrator.py")
    if app not in cfgs:
        print("ERROR: unknown app:", app)
        return 2
    cfg = cfgs[app]

    model = os.getenv("TESTCEPTION_LLM_MODEL", "").strip()
    key = os.getenv("OPENROUTER_API_KEY", "").strip()

    if not reuse_plan and (not model or not key):
        print(
            "ERROR: TESTCEPTION_LLM_MODEL and OPENROUTER_API_KEY "
            "must be set for a fresh global LLM call."
        )
        return 2

    try:
        cap = max(0, int(os.getenv("TESTCEPTION_MAX_SCENARIOS", "0")))
        max_out = max(1000, int(os.getenv("TESTCEPTION_GLOBAL_MAX_OUTPUT_TOKENS", "12000")))
    except ValueError:
        print("ERROR: scenario/output token settings must be integers.")
        return 2

    crawl = resolve_crawl_dir(repo, app, cfg)
    results = repo / "global-baseline-results" / app
    results.mkdir(parents=True, exist_ok=True)

    all_elements, states, elements_by_state = collect_states(crawl, cfg)
    available = {s["state_name"] for s in states}
    edges, edge_map = load_edges(crawl, cfg, available)
    start = choose_start(available, edges)
    global_map = {e["global_id"]: e for e in all_elements}

    prompt = build_prompt(app, start, states, edge_map, cfg.get("app_hints", []) or [], cap)
    (results / "global-prompt.txt").write_text(prompt, encoding="utf-8")

    base_metrics = {
        "planner": "single_global_context_baseline_v2",
        "application": app,
        "model": model,
        "temperature": 0.2,
        "state_count": len(states),
        "edge_count": len(edges),
        "actionable_element_count": len(all_elements),
        "start_state": start,
        "prompt_chars": len(prompt),
        "prompt_bytes_utf8": len(prompt.encode("utf-8")),
        "max_scenarios": cap or None,
        "max_output_tokens": max_out,
        "deterministic_fallback_enabled": False,
        "shortest_path_repair_enabled": False,
        "initial_entry_prefix_materialization_enabled": True,
        "later_state_jump_repair_enabled": False,
        "executable_prefix_salvage_enabled": True,
        "planning_llm_calls_target": 1,
        "plan_source": "reused_existing_plan" if reuse_plan else "fresh_global_llm",
    }

    print("=" * 70)
    print("SINGLE GLOBAL CONTEXT BASELINE")
    print("application      :", app)
    print("states           :", len(states))
    print("edges            :", len(edges))
    print("actionable elems :", len(all_elements))
    print("prompt chars     :", f"{len(prompt):,}")
    print("start state      :", start)
    print("LLM calls        : 1")
    print("=" * 70)

    plan_file = results / "global-plan.json"
    previous_metrics_file = results / "global-planner-metrics.json"

    if reuse_plan:
        if not plan_file.is_file():
            print("ERROR: --reuse-plan requested but no existing plan found:")
            print(plan_file)
            return 2

        plan = GlobalPlan.model_validate(
            json.loads(plan_file.read_text(encoding="utf-8"))
        )

        previous_metrics = {}
        if previous_metrics_file.is_file():
            try:
                previous_metrics = json.loads(
                    previous_metrics_file.read_text(encoding="utf-8")
                )
            except Exception:
                previous_metrics = {}

        if not model:
            model = str(
                previous_metrics.get("model")
                or "reused-existing-plan"
            )
            base_metrics["model"] = model

        llm_metrics = {
            "llm_calls_attempted": previous_metrics.get(
                "llm_calls_attempted", 1
            ),
            "llm_calls_completed": previous_metrics.get(
                "llm_calls_completed", 1
            ),
            "elapsed_seconds": previous_metrics.get("elapsed_seconds"),
            "input_tokens": previous_metrics.get("input_tokens"),
            "llm_call_performed_this_run": False,
        }

        print("GLOBAL_PLAN_SOURCE=REUSED_EXISTING_PLAN")
        print("No OpenRouter call will be made.")

    else:
        started = time.perf_counter()
        try:
            plan, llm_metrics = one_global_call(
                model,
                key,
                prompt,
                max_out,
            )
            llm_metrics["llm_call_performed_this_run"] = True
        except Exception as exc:
            metrics = {
                **base_metrics,
                "status": "FAILED",
                "failure_category": fail_category(exc),
                "failure_type": type(exc).__name__,
                "failure_message": str(exc),
                "llm_calls_attempted": 1,
                "llm_calls_completed": 0,
                "llm_call_performed_this_run": True,
                "elapsed_seconds": time.perf_counter() - started,
            }
            dump(
                results / "global-planner-metrics.json",
                metrics,
            )
            print("GLOBAL_PLANNER_STATUS=FAILED")
            print("reason=" + metrics["failure_category"])
            print(
                "metrics="
                + str(results / "global-planner-metrics.json")
            )
            return 3

        dump(plan_file, plan.model_dump())

    valid, validation = validate(plan, start, global_map, elements_by_state, edge_map, cfg, cap)
    dump(results / "validation-report.json", validation)

    if not valid:
        dump(results / "global-planner-metrics.json", {
            **base_metrics,
            **llm_metrics,
            "status": "FAILED",
            "failure_category": "NO_GRAPH_VALID_SCENARIOS",
            **{k: validation[k] for k in (
                "raw_scenario_count",
                "valid_scenario_count",
                "complete_valid_scenario_count",
                "truncated_valid_scenario_count",
                "rejected_scenario_count",
                "rejection_reasons",
                "truncation_reasons",
                "entry_prefix_edges_inserted_total",
            )},
        })
        print("GLOBAL_PLANNER_STATUS=FAILED")
        print("reason=NO_GRAPH_VALID_SCENARIOS")
        return 4

    output = (
        repo / "dante" / "applications" / app
        / f"testsuite-{app}-global" / "src" / "main" / "java" / "tests"
    )
    generate_dante_suites(
        valid,
        app_name=app,
        login_config=cfg.get("login"),
        wait_time=cfg.get("wait_time", 1000),
        start_url=cfg.get("start_url"),
        output_dir=output,
    )

    strategy = "Checked" if app in {"phoenix", "splittypie"} else "Fired"
    java_file = output / f"GeneratedTestSuite{strategy}.java"
    test_count = count_java_tests(java_file)

    dump(results / "global-planner-metrics.json", {
        **base_metrics,
        **llm_metrics,
        "status": "SUCCESS",
        "raw_scenario_count": validation["raw_scenario_count"],
        "valid_scenario_count_before_generator": len(valid),
        "complete_valid_scenario_count": validation[
            "complete_valid_scenario_count"
        ],
        "truncated_valid_scenario_count": validation[
            "truncated_valid_scenario_count"
        ],
        "rejected_scenario_count": validation[
            "rejected_scenario_count"
        ],
        "rejection_reasons": validation["rejection_reasons"],
        "truncation_reasons": validation["truncation_reasons"],
        "entry_prefix_edges_inserted_total": validation[
            "entry_prefix_edges_inserted_total"
        ],
        "generated_java_test_count_after_generator_reduction": test_count,
        "generated_java_file": str(java_file),
    })

    print("GLOBAL_PLANNER_STATUS=SUCCESS")
    print("valid_scenarios=", len(valid))
    print("generated_tests=", test_count)
    print("legacy_suite=", java_file)
    print("metrics=", results / "global-planner-metrics.json")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
