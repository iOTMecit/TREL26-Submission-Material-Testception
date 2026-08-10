import os
import json
import sys
import ast
import re
from crawljax_parser import extract_actionable_skeleton, generate_worker_prompt
from worker_agent import ask_worker_llm, build_fallback_decision, validate_llm_decision
from dante_suite_generator import generate_dante_suites
from pathlib import Path


PROJECT_CONFIGS = {
    "ecommerce": {
        "strategy": "Fired",
        "has_crawl0": True,
        "wait_time": 250,
        "ignore_xpaths": [
            "//DIV[@id = 'carousel-example-generic']/A[1]",
            "//DIV[@id = 'carousel-example-generic']/A[2]"
        ],
        "login": None
    },
    "dimeshift": {
        "strategy": "Fired",
        "has_crawl0": True,
        "wait_time": 250,
        "ignore_xpaths": [
            "/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[9]/A[1]",
            "/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[10]/A[1]"
        ],
        "login": {
            "user_xpath": "//INPUT[@id='input_username']",
            "user_val": "asd@asd.com",
            "pass_xpath": "/HTML[1]/BODY[1]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]",
            "pass_val": "adminadmin",
            "submit_type": "click",
            "submit_xpath": "//INPUT[@id='signin_modal_form_submit']"
        },
        "app_hints": [
            "Dimeshift is an expense/wallet tracking app.",
            "Login is already configured as a precondition. Do not repeatedly test Sign In or Register unless the current state is explicitly unauthenticated.",
            "On the public landing page, the important entry actions are demo signup buttons, Register, Sign In, and screenshot/navigation links; business flows should be preferred after login.",
            "After login, prioritize Wallets, Goals, Add Wallet, wallet details, transactions, import, add transaction, set goal, filters, access filters, active/trash views, and shared/yours views.",
            "The first Add button on the wallet list usually opens an Add Wallet modal/form. Treat this Add as add_or_open and click it with safeClick, not as final submit.",
            "Inside Add Wallet or similar modal forms, fill required-looking fields such as Name and Currency before clicking the final Add/Save submit.",
            "On wallet detail pages, prioritize Transactions, Overview, Import, transaction rows/details, Month dropdown, Set Goal, and wallet edit/delete/share-like controls if present.",
            "Header/navbar containers are not commit buttons. Do not treat the whole header text as Save/Submit.",
            "Source Code, Contact, API, Settings, GitHub and footer links are lower priority than wallet/transaction/goal business flows."
        ]
    },
    "retroboard": {
        "strategy": "Fired",
        "has_crawl0": True,
        "wait_time": 250,
        "ignore_xpaths": [],
        "login": {
            "user_xpath": "/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]",
            "user_val": "user",
            "pass_xpath": None,
            "pass_val": None,
            "submit_type": "enter"
        },
        "app_hints": [
            "Retroboard is a retrospective board app.",
            "The initial screen asks for language and user name. Fill the name field before clicking LET'S START.",
            "After entering, prioritize Create/New Session, Previous, Advanced, session board creation, and retrospective board interactions.",
            "On the main board, prioritize columns such as What went well, What could be improved, and A brilliant idea to share.",
            "Cards/notes inside columns are high-value. Prefer adding notes/cards, editing note text if possible, and deleting notes as CRUD actions.",
            "Delete buttons inside note cards are meaningful destructive actions; use them after notes/cards exist or when already present in the crawled state.",
            "Language selection is useful once, but should not dominate over session creation and board/card interactions.",
            "Menu/user/share icons are lower priority unless no board/card actions remain.",
            "For happy-path create-session flows, fill required-looking fields before clicking Create/New Session."
        ]
    },
    "phoenix": {
        "strategy": "Checked",
        "has_crawl0": True,
        "wait_time": 250,
        "ignore_xpaths": [
            "//A[contains(normalize-space(.), '@bigardone')]",
            "//A[contains(normalize-space(.), 'Trello')]",
            "//A[contains(normalize-space(.), 'Diacode')]"
        ],
        "ignore_texts": [
            "@bigardone",
            "bigardone",
            "Trello",
            "Diacode"
        ],
        "ignore_href_contains": [
            "twitter.com/bigardone",
            "trello.com",
            "diacode.com"
        ],
        "login": {
            "user_xpath": "/HTML[1]/BODY[1]/MAIN[1]/DIV[1]/DIV[1]/MAIN[1]/FORM[1]/DIV[1]/INPUT[1]",
            "user_val": "john@phoenix-trello.com",
            "pass_xpath": "/HTML[1]/BODY[1]/MAIN[1]/DIV[1]/DIV[1]/MAIN[1]/FORM[1]/DIV[2]/INPUT[1]",
            "pass_val": "12345678",
            "submit_type": "click",
            "submit_xpath": "/HTML[1]/BODY[1]/MAIN[1]/DIV[1]/DIV[1]/MAIN[1]/FORM[1]/BUTTON[1]"
        },
        "app_hints": [
            "Phoenix is a board/list/card management app.",
            "The application has both public account creation flow and authenticated board/card flows.",
            "Test the public Create new account / Sign up flow at least once before focusing only on authenticated board flows.",
            "For Create new account, click Sign Up, fill First name, Last name, Email, Password, Confirm password, then click Sign up.",
            "Login is configured as a precondition for authenticated business flows with john@phoenix-trello.com / 12345678.",
            "After authenticated login, prioritize Boards, New Board, board name form, Create Board, opening boards, lists, cards, card details, edit, delete, move, and modal dialogs.",
            "On the My Boards page, New Board and Board Name are high-value. Fill Board Name before clicking Create Board.",
            "Sign out and icon/profile actions are lower priority than board/list/card CRUD.",
            "For happy-path forms, submit must be the final action after all required-looking fields are filled.",
            "Do not treat the Phoenix header/logo/navbar/footer/profile links as commit buttons."
        ]

    },

    "petclinic": {
        "strategy": "Fired",
        "has_crawl0": True,
        "wait_time": 250,
        "start_url": "http://localhost:3000/",
        "strict_graph_guided_elements": True,
        "preserve_edge_input_values": True,
        "input_overrides": {
            "telephone": "5551234567",
        },
        "ignore_xpaths": [],
        "login": None,
        "app_hints": [
            "Petclinic is a veterinary clinic CRUD app. There is no login precondition.",
            "Pet Type and Specialty must exist before Pet and Veterinarian creation.",
            "Prioritize business flows under Owners, Veterinarians, Pet Types, and Specialties.",
            "For happy-path owner forms, fill all visible required-looking fields before clicking Add Owner or Update Owner.",
            "Important pet flows: add/edit pet, fill pet name, birth date, type, owner-related fields if present, then submit.",
            "Important visit flows: add visit, fill date and description, then submit.",
            "Important veterinarian/specialty/pet-type flows: Add, Edit, Delete, update name/type fields, then submit.",
            "Keep one negative validation scenario for owner/pet/specialty forms if required-looking fields are visible."
        ]
    },
    "splittypie": {
        "strategy": "Checked",
        "has_crawl0": True,
        "wait_time": 250,
        "ignore_xpaths": [
            "//a[contains(text(), 'Language')]",
            "//a[contains(text(), 'About')]"
        ],
        "login": None,
        "app_hints": [
            "Splittypie is an expense splitting app. There is no login precondition.",
            "Prioritize Create New Event, event creation form, participants, currency, Save, event overview, transactions, share, edit, and add transaction flows.",
            "On the landing page, Create New Event is the main business entry point. About, Features, Source, GitHub and social links are lower priority.",
            "For the event creation form, fill Event Name, choose Currency if available, fill at least two participant name fields, then click Save.",
            "Add Participant is not a final submit. It should be clicked with safeClick to add more participant fields.",
            "Save is the final commit for event creation and must happen after required-looking fields are filled.",
            "After event creation, prioritize Overview, Transactions, Share, Edit, floating + button, Add your first transaction, transaction forms, and settlement/balance sections.",
            "On event detail pages, floating + button usually opens transaction/add modal. Treat it as add_or_open, not final submit.",
            "Share and Edit are meaningful business actions. Test them before static/footer links.",
            "Do not repeatedly test About/Features/Source if event creation and transaction flows are still available."
        ]
    }
}


if len(sys.argv) < 2:
    print("❌ HATA: Lütfen terminalden bir proje adı girin! Örn: python3 mentor_orchestrator.py splittypie")
    sys.exit(1)

APP_NAME = sys.argv[1].lower()

if APP_NAME not in PROJECT_CONFIGS:
    print(f"❌ HATA: '{APP_NAME}' konfigürasyonu bulunamadı.")
    sys.exit(1)

config = PROJECT_CONFIGS[APP_NAME]

RLM_PROJECT_DIR = Path(__file__).resolve().parent
REPO_ROOT = RLM_PROJECT_DIR.parents[1]
DANTE_ROOT = REPO_ROOT / "dante"

BASE_CRAWL_DIR = (
    DANTE_ROOT
    / "applications"
    / APP_NAME
    / "localhost"
    / "crawl-with-inputs"
)

if not BASE_CRAWL_DIR.exists():
    raise FileNotFoundError(
        f"Crawl directory bulunamadı: {BASE_CRAWL_DIR}"
    )

GENERATED_TEST_DIR = (
    DANTE_ROOT
    / "applications"
    / APP_NAME
    / f"testsuite-{APP_NAME}"
    / "src"
    / "main"
    / "java"
    / "tests"
)

TRANSITION_MAP = {}
TRANSITION_EDGE_MAP = {}
STATE_CANDIDATE_XPATHS = {}
STATE_EDGES = {}
AVAILABLE_DOM_STATES = set()

memory_visited_ids = {}
state_action_memory = {}
state_feature_memory = {}
all_test_scenarios = []
seen_scenario_signatures = set()
dead_end_click_memory = {}
same_state_continue_memory = {}
explored_transition_edges = set()
dom_only_attempted_actions = set()

def make_transition_key(edge_info):
    if not edge_info:
        return None

    return (
        edge_info.get("from", ""),
        normalize_xpath(edge_info.get("xpath", "")),
        edge_info.get("to", ""),
    )


def is_transition_explored(edge_info):
    key = make_transition_key(edge_info)
    return key is not None and key in explored_transition_edges


def remember_transition_explored(edge_info):
    key = make_transition_key(edge_info)
    if key is not None:
        explored_transition_edges.add(key)

def get_unexplored_recorded_edges(current_state):
    """
    Return unique result.json edges that have not yet been consumed
    by the recursive exploration.
    """
    state_id = current_state.replace(".html", "")
    unique_edges = {}

    for edge in STATE_EDGES.get(state_id, []):
        if is_transition_explored(edge):
            continue

        key = make_transition_key(edge)

        if key is not None:
            unique_edges[key] = edge

    return list(unique_edges.values())


def get_exploration_mode(current_state):
    unexplored_edges = get_unexplored_recorded_edges(
        current_state
    )

    if unexplored_edges:
        return "GRAPH_FIRST"

    return "DOM_FALLBACK"

def normalize_xpath(xp):
    if not xp:
        return ""
    return str(xp).replace(" ", "").replace("xpath", "").strip().lower()


def normalize_text(text):
    if not text:
        return ""
    text = str(text).strip().lower()
    text = re.sub(r"\s+", " ", text)
    return text


def should_ignore_element(el, config):
    def n(v):
        return normalize_text(str(v or ""))

    text_blob = n(" ".join([
        el.get("text", ""),
        el.get("label", ""),
        el.get("aria_label", ""),
        el.get("title", ""),
        el.get("title_attr", ""),
        el.get("href", ""),
        el.get("id", ""),
        el.get("name", ""),
        el.get("class", ""),
        el.get("xpath", ""),
        el.get("absolute_xpath", ""),
    ]))

    xpath_blob = " ".join([
        str(el.get("xpath", "") or ""),
        str(el.get("absolute_xpath", "") or ""),
        str(el.get("robust_xpath", "") or ""),
    ]).lower()

    for ignored_text in config.get("ignore_texts", []):
        if n(ignored_text) and n(ignored_text) in text_blob:
            return True

    for ignored_href in config.get("ignore_href_contains", []):
        if n(ignored_href) and n(ignored_href) in text_blob:
            return True

    for ignored_xpath in config.get("ignore_xpaths", []):
        ignored_xpath_l = str(ignored_xpath or "").lower()

        # Exact/simple xpath match
        if ignored_xpath_l and ignored_xpath_l in xpath_blob:
            return True

        # Support simple contains(normalize-space(.), 'TEXT') style ignore_xpaths
        m = re.search(r"contains\s*\(\s*normalize-space\(\.\)\s*,\s*['\"]([^'\"]+)['\"]\s*\)", ignored_xpath_l)
        if m and n(m.group(1)) in text_blob:
            return True

    return False
def xpath_literal(value):
    value = value or ""

    if "'" not in value:
        return f"'{value}'"

    if '"' not in value:
        return f'"{value}"'

    parts = value.split("'")
    return "concat(" + ", \"'\", ".join([f"'{p}'" for p in parts]) + ")"


def looks_invalid_xpath(xpath):
    xp = (xpath or "").strip()

    if not xp:
        return True

    low = xp.lower()

    if "@id = 'el_" in low or '@id="el_' in low:
        return True

    if re.search(r"@id\s*=\s*['\"]ember\d+['\"]", low):
        return True

    return False



def choose_replay_value_for_xpath(xpath, raw_value, input_index=None):
    value = str(raw_value or "").strip()
    if not value:
        return ""

    # Crawljax stores SELECT values as a comma-separated candidate list.
    # Selenium Select.selectByVisibleText needs exactly one visible option.
    if "/SELECT" in str(xpath).upper():
        options = [opt.strip() for opt in value.split(",") if opt.strip()]
        if not options:
            return value

        preferred_options = [
            "Euro (EUR)",
            "United States dollar (USD)",
            "Pound sterling (GBP)",
        ]

        for preferred in preferred_options:
            if preferred in options:
                return preferred

        return options[0]

    # Do not replay Crawljax's random strings directly. They make generated
    # scenarios look different even when the tested behavior is identical.
    deterministic_inputs = {
        1: "QA Test Event",
        2: "Alice",
        3: "Bob",
        4: "Charlie",
        5: "Delta",
    }

    if input_index in deterministic_inputs:
        return deterministic_inputs[input_index]

    return "QA Test Value"


def replay_value_for_identifier(
    identification_type,
    identifier,
    xpath,
    raw_value,
    input_index=None,
):
    """
    Keep recorded Petclinic form values because they contain valid reference
    data such as BootstrapType/BootstrapSpecialty. Other applications retain
    the previous deterministic replay behavior.
    """
    value = str(raw_value or "").strip()
    if not value:
        return ""

    normalized_identifier = normalize_text(identifier)

    if config.get("preserve_edge_input_values", False):
        override = (
            config.get("input_overrides", {})
            .get(normalized_identifier)
        )

        if override is not None:
            return str(override)

        if normalized_identifier == "telephone":
            digits = re.sub(r"\D", "", value)
            return digits or "5551234567"

        return value

    return choose_replay_value_for_xpath(
        xpath,
        value,
        input_index=input_index,
    )


def parse_edge_input_values(raw_input_values):
    """
    Parse Crawljax edge inputValues using xpath##, id## and name## locators.

    Examples:
      id##firstName##RLMOwner
      name##birthDate##2021-06-09
      xpath##/HTML/.../INPUT[1]##2026-07-24

    A regex is used instead of splitting only on ':xpath##', because Petclinic
    stores most form values with id/name locators.
    """
    raw = str(raw_input_values or "").strip()

    if not raw or raw.lower() == "none":
        return []

    token_pattern = re.compile(
        r"(?:^|:)(xpath|id|name)##(.*?)##(.*?)(?=:(?:xpath|id|name)##|$)",
        re.IGNORECASE,
    )

    parsed_steps = []
    input_counter = 0

    for match in token_pattern.finditer(raw):
        identification_type = normalize_text(match.group(1))
        identifier = str(match.group(2) or "").strip()
        raw_value = str(match.group(3) or "").strip()

        if not identifier or not raw_value:
            continue

        if identification_type == "xpath":
            xpath = identifier
        elif identification_type == "id":
            xpath = f"//*[@id = {xpath_literal(identifier)}]"
        elif identification_type == "name":
            xpath = f"//*[@name = {xpath_literal(identifier)}]"
        else:
            continue

        input_counter += 1
        value = replay_value_for_identifier(
            identification_type,
            identifier,
            xpath,
            raw_value,
            input_index=input_counter,
        )

        if not value:
            continue

        parsed_steps.append({
            "xpath": xpath,
            "action": "input",
            "input_value": value,
            "source": "result_json_inputValues",
            "identification_type": identification_type,
            "identifier": identifier,
        })

    return parsed_steps


def match_element_for_replay(elements, replay):
    identification_type = normalize_text(
        replay.get("identification_type", "")
    )
    identifier = str(replay.get("identifier", "") or "").strip()

    if identification_type == "id":
        for element in elements:
            if str(element.get("id_attr", "") or "").strip() == identifier:
                return element

    if identification_type == "name":
        for element in elements:
            if str(element.get("name_attr", "") or "").strip() == identifier:
                return element

    return match_element_by_xpath(
        elements,
        replay.get("xpath", ""),
    )

def match_element_by_xpath(elements, xpath):
    target = normalize_xpath(xpath)

    for el in elements:
        candidates = [
            el.get("xpath", ""),
            el.get("absolute_xpath", ""),
        ]

        for candidate in candidates:
            if normalize_xpath(candidate) == target:
                return el

    return None


def edge_replay_steps(edge_info, current_state, elements, scenario_reason):
    if not edge_info:
        return []

    steps = []

    for idx, replay in enumerate(edge_info.get("input_steps", [])):
        replay_xpath = replay.get("xpath", "")
        action = replay.get("action", "input")
        input_value = replay.get("input_value", "")
        identification_type = normalize_text(
            replay.get(
                "identification_type",
                "",
            )
        )

        identifier = str(
            replay.get(
                "identifier",
                "",
            )
            or ""
        ).strip()

        if (
            identification_type == "id"
            and identifier.isdigit()
        ):
            continue        
        matched = match_element_for_replay(elements, replay)

        if matched:
            matched_tag = normalize_text(matched.get("tag", ""))
            if matched_tag in {"select", "mat-select"}:
                action = "select"
            elif matched_tag in {"input", "textarea"}:
                action = "input"

            stable_xpath = (
                matched.get("xpath")
                or matched.get("absolute_xpath")
                or replay_xpath
            )
            input_value = apply_configured_input_override(
                matched,
                input_value,
            )
        else:
            continue

        steps.append({
            "selected_id": (
                matched.get("llm_id")
                if matched
                else f"edge_input_{idx}"
            ),
            "element": (
                (
                    matched.get("text")
                    or matched.get("placeholder")
                    or matched.get("label_text")
                    or matched.get("id_attr")
                    or matched.get("name_attr")
                )
                if matched
                else "result.json inputValues replay"
            ),
            "tag": (
                matched.get(
                    "tag",
                    "select" if action == "select" else "input",
                )
                if matched
                else ("select" if action == "select" else "input")
            ),
            "type_attr": matched.get("type_attr", "") if matched else "",
            "xpath": stable_xpath,
            "action": action,
            "input_value": input_value,
            "options": matched.get("options", []) if matched else [],
            "feature_bucket": "result_json_input_replay",
            "feature_signature": (
                f"edge_replay:{action}:"
                f"{normalize_xpath(stable_xpath)}:"
                f"{normalize_text(input_value)[:30]}"
            ),
            "state": current_state,
            "state_type": current_state,
            "scenario_reason": (
                scenario_reason
                or "Crawljax result.json inputValues replay before edge click."
            ),
            "edge_replay": True,
        })

    return steps


def append_unique_replay_steps(path, replay_steps):
    for replay_step in replay_steps:
        replay_key = (
            replay_step.get("state", ""),
            replay_step.get("action", ""),
            normalize_xpath(replay_step.get("xpath", "")),
        )

        existing_step = next(
            (
                step
                for step in path
                if (
                    step.get("state", ""),
                    step.get("action", ""),
                    normalize_xpath(step.get("xpath", "")),
                ) == replay_key
            ),
            None,
        )

        if existing_step:
            if (
                config.get("preserve_edge_input_values", False)
                and replay_step.get("action") in {"input", "select"}
            ):
                existing_step["input_value"] = replay_step.get(
                    "input_value",
                    existing_step.get("input_value", ""),
                )
                existing_step["feature_signature"] = replay_step.get(
                    "feature_signature",
                    existing_step.get("feature_signature", ""),
                )
                print(
                    f"   🔁 result.json değeri mevcut forma uygulandı: "
                    f"{replay_step.get('action', '').upper()} -> "
                    f"{replay_step.get('xpath', '')} "
                    f"value={repr(replay_step.get('input_value', ''))}"
                )
            continue

        path.append(replay_step)
        print(
            f"   🔁 result.json inputValues replay: "
            f"{replay_step.get('action', '').upper()} -> "
            f"{replay_step.get('xpath', '')} "
            f"value={repr(replay_step.get('input_value', ''))}"
        )

    return path


def find_transition_edge(current_state_name, target_xpath, chosen_text="", extra_xpath_candidates=None):
    state_id = current_state_name.replace(".html", "")

    candidates = [target_xpath]
    candidates.extend(extra_xpath_candidates or [])

    for candidate in candidates:
        clean_candidate = normalize_xpath(candidate)
        if not clean_candidate:
            continue

        edge_info = TRANSITION_EDGE_MAP.get((state_id, clean_candidate))
        if edge_info:
            print(
                f"➡️ JSON edge bulundu: state={state_id}, "
                f"xpath={clean_candidate}, next={edge_info['to']} "
                f"(original={edge_info.get('to_original', edge_info['to'])}, "
                f"resolve={edge_info.get('target_resolve_reason', 'n/a')}), "
                f"inputValues={len(edge_info.get('input_steps', []))}"
            )
            return edge_info

    clean_text = normalize_text(chosen_text)
    if clean_text:
        for edge in STATE_EDGES.get(state_id, []):
            edge_text = edge.get("text", "")
            if edge_text and (clean_text == edge_text or clean_text in edge_text or edge_text in clean_text):
                print(
                    f"➡️ TEXT fallback edge bulundu: state={state_id}, "
                    f"text={clean_text}, next={edge['to']} "
                    f"(original={edge.get('to_original', edge['to'])}, "
                    f"resolve={edge.get('target_resolve_reason', 'n/a')}), "
                    f"inputValues={len(edge.get('input_steps', []))}"
                )
                return edge

    return None


def summarize_outgoing_edges_for_prompt(current_state_name, limit=12):
    state_id = current_state_name.replace(".html", "")
    edges = STATE_EDGES.get(state_id, [])[:limit]

    summary = []
    for idx, edge in enumerate(edges):
        summary.append({
            "edge_index": idx,
            "text": edge.get("raw_text", ""),
            "to": edge.get("to", ""),
            "to_original": edge.get("to_original", ""),
            "target_dom_resolved": edge.get("target_dom_resolved", False),
            "target_resolve_reason": edge.get("target_resolve_reason", ""),
            "target_self_loop_due_to_missing": edge.get("target_self_loop_due_to_missing", False),
            "click_xpath": edge.get("raw_xpath", ""),
            "input_values_count": len(edge.get("input_steps", [])),
            "input_values_preview": [
                {
                    "action": step.get("action"),
                    "xpath": step.get("xpath"),
                    "value": step.get("input_value"),
                }
                for step in edge.get("input_steps", [])[:4]
            ],
        })

    return summary



def collect_available_dom_states(base_crawl_dir):
    doms_folder = os.path.join(base_crawl_dir, "doms")
    if not os.path.isdir(doms_folder):
        return set()

    return {
        filename.replace(".html", "")
        for filename in os.listdir(doms_folder)
        if filename.endswith(".html")
    }


def resolve_existing_target_state(to_state, states_data, available_dom_states):
    """
    Crawljax/DANTE may keep states in result.json and screenshots while the corresponding
    DOM file is not present under crawl0/doms. In that case, following the raw edge
    causes: DOM bulunamadı: .../doms/stateXX.html.

    This resolver redirects such pruned/missing targets to an available near-duplicate
    DOM when possible. If no usable DOM can be found, the edge is still kept, but it is
    marked as unresolved so recursion can stop safely instead of crashing.
    """
    if not to_state:
        return "", False, "empty_target"

    if to_state in available_dom_states:
        return f"{to_state}.html", True, "exact_dom"

    visited = set()
    cursor = to_state

    # Follow Crawljax's nearestState chain if it points to a DOM that actually exists.
    for _ in range(10):
        if not cursor or cursor in visited:
            break
        visited.add(cursor)

        state_info = states_data.get(cursor, {})
        nearest = state_info.get("nearestState")

        if not nearest or nearest == "null":
            break

        if nearest in available_dom_states:
            return f"{nearest}.html", True, f"nearest_dom_for_missing:{to_state}->{nearest}"

        cursor = nearest

    # Fallback: find another available state with the same URL.
    target_url = states_data.get(to_state, {}).get("url", "")
    if target_url:
        for candidate in available_dom_states:
            if states_data.get(candidate, {}).get("url", "") == target_url:
                return f"{candidate}.html", True, f"same_url_dom_for_missing:{to_state}->{candidate}"

    return f"{to_state}.html", False, f"missing_dom:{to_state}"


def should_replace_edge(existing_edge, new_edge):
    """
    Multiple result.json edges can share the same (from_state, xpath) but point to
    different Crawljax states. Prefer an edge whose target DOM can actually be opened.
    Also avoid choosing an edge that resolves back to the source state because its
    original target DOM is missing; those edges are valid replay leafs but bad
    recursion targets.
    """
    if existing_edge is None:
        return True

    existing_self_loop_missing = bool(existing_edge.get("target_self_loop_due_to_missing", False))
    new_self_loop_missing = bool(new_edge.get("target_self_loop_due_to_missing", False))

    if existing_self_loop_missing and not new_self_loop_missing:
        return True

    if new_self_loop_missing and not existing_self_loop_missing:
        return False

    existing_ok = bool(existing_edge.get("target_dom_resolved", False))
    new_ok = bool(new_edge.get("target_dom_resolved", False))

    if new_ok and not existing_ok:
        return True

    if existing_ok and not new_ok:
        return False

    # Prefer edges with replayable inputValues because they reproduce the transition better.
    if len(new_edge.get("input_steps", [])) > len(existing_edge.get("input_steps", [])):
        return True

    return False

def _state_numeric_id(state_name):
    match = re.fullmatch(r"state(\d+)", str(state_name or ""))
    return int(match.group(1)) if match else 10**9


def choose_effective_start_state(states_data):
    """
    Use index when it owns transitions. When index is only an isolated
    bootstrap snapshot, select the closest same-page state that has edges.
    """
    if "index" in AVAILABLE_DOM_STATES and STATE_EDGES.get("index"):
        return "index.html"

    index_info = states_data.get("index", {}) or {}
    index_url = str(index_info.get("url", "") or "")
    candidates = []

    for state_name, state_info in states_data.items():
        if state_name == "index" or state_name not in AVAILABLE_DOM_STATES:
            continue

        outgoing_count = len(STATE_EDGES.get(state_name, []))
        if outgoing_count <= 0:
            continue

        state_url = str(state_info.get("url", "") or "")
        same_url = bool(index_url and state_url == index_url)
        nearest_is_index = str(
            state_info.get("nearestState", "") or ""
        ) == "index"

        if not same_url and not nearest_is_index:
            continue

        candidates.append(
            (
                state_name,
                nearest_is_index,
                same_url,
                outgoing_count,
                len(state_info.get("candidateElements", []) or []),
                int(state_info.get("timeAdded", 0) or 0),
            )
        )

    if candidates:
        chosen = max(
            candidates,
            key=lambda item: (
                int(item[1]),
                int(item[2]),
                item[3],
                item[4],
                -_state_numeric_id(item[0]),
                -item[5],
            ),
        )[0]

        print(
            "🚦 index state'i geçişsiz; etkin graph başlangıcı "
            f"{chosen}.html olarak seçildi. "
            f"outgoing={len(STATE_EDGES.get(chosen, []))}"
        )
        return f"{chosen}.html"

    fallback = [
        name
        for name in AVAILABLE_DOM_STATES
        if STATE_EDGES.get(name)
    ]

    if fallback:
        chosen = min(
            fallback,
            key=lambda name: (
                int(
                    (states_data.get(name, {}) or {}).get(
                        "timeAdded",
                        0,
                    )
                    or 0
                ),
                _state_numeric_id(name),
            ),
        )
        print(
            "🚦 index state'i geçişsiz; ilk geçişli state seçildi: "
            f"{chosen}.html"
        )
        return f"{chosen}.html"

    print("⚠️ Geçişli başlangıç state'i bulunamadı; index.html kullanılacak.")
    return "index.html"


def load_transition_map(base_crawl_dir):
    global TRANSITION_MAP, TRANSITION_EDGE_MAP, STATE_CANDIDATE_XPATHS, STATE_EDGES, AVAILABLE_DOM_STATES

    TRANSITION_MAP = {}
    TRANSITION_EDGE_MAP = {}
    STATE_CANDIDATE_XPATHS = {}
    STATE_EDGES = {}

    AVAILABLE_DOM_STATES = collect_available_dom_states(base_crawl_dir)

    json_path = os.path.join(base_crawl_dir, "result.json")

    if not os.path.exists(json_path):
        print(f"⚠️ HATA: {json_path} bulunamadı!")
        return "index.html"

    with open(json_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    states_data = data.get("states", {})
    edges = data.get("edges", [])
    loaded_count = 0
    missing_target_count = 0
    redirected_target_count = 0

    for edge in edges:
        if not isinstance(edge, dict):
            continue

        from_state = edge.get("from")
        to_state = edge.get("to")
        raw_xpath = edge.get("id", "")
        xpath_key = normalize_xpath(raw_xpath)
        text_key = normalize_text(edge.get("text", ""))
        input_steps = parse_edge_input_values(edge.get("inputValues", ""))

        if from_state and to_state and xpath_key:
            resolved_to, target_dom_resolved, resolve_reason = resolve_existing_target_state(
                to_state,
                states_data,
                AVAILABLE_DOM_STATES,
            )

            if not target_dom_resolved:
                missing_target_count += 1
            elif str(resolve_reason).startswith(("nearest_dom_for_missing", "same_url_dom_for_missing")):
                redirected_target_count += 1

            resolved_state_id = resolved_to.replace(".html", "") if resolved_to else ""
            # If a missing target is redirected back to the same source DOM, following it
            # would make the mentor analyze the exact same DOM again and again.
            # Example in Splittypie: state5 --Save--> state15, but state15.html is absent
            # and nearestState points back to state5. This is useful for replaying the
            # edge as a leaf scenario, but it must not be used for recursive descent.
            target_self_loop_due_to_missing = (
                resolved_state_id == from_state
                and to_state != from_state
                and str(resolve_reason).startswith((
                    "nearest_dom_for_missing",
                    "same_url_dom_for_missing",
                ))
            )

            edge_info = {
                "from": from_state,
                "to": resolved_to,
                "to_original": f"{to_state}.html",
                "target_dom_resolved": target_dom_resolved,
                "target_resolve_reason": resolve_reason,
                "target_self_loop_due_to_missing": target_self_loop_due_to_missing,
                "xpath": xpath_key,
                "raw_xpath": raw_xpath.replace("xpath ", "", 1).strip(),
                "text": text_key,
                "raw_text": edge.get("text", ""),
                "eventType": edge.get("eventType", ""),
                "raw_inputValues": edge.get("inputValues", ""),
                "input_steps": input_steps,
            }

            map_key = (from_state, xpath_key)
            existing_edge = TRANSITION_EDGE_MAP.get(map_key)

            if should_replace_edge(existing_edge, edge_info):
                TRANSITION_MAP[map_key] = resolved_to
                TRANSITION_EDGE_MAP[map_key] = edge_info

            STATE_CANDIDATE_XPATHS.setdefault(from_state, set()).add(xpath_key)
            STATE_EDGES.setdefault(from_state, []).append(edge_info)
            loaded_count += 1

    print(
        f"✅ result.json haritası yüklendi: {loaded_count} yol tanımlandı. "
        f"DOM states={len(AVAILABLE_DOM_STATES)}, "
        f"redirected_missing_targets={redirected_target_count}, "
        f"unresolved_missing_targets={missing_target_count}"
    )

    return choose_effective_start_state(states_data)


def find_next_state(current_state_name, target_xpath, chosen_text="", extra_xpath_candidates=None):
    edge_info = find_transition_edge(
        current_state_name,
        target_xpath,
        chosen_text=chosen_text,
        extra_xpath_candidates=extra_xpath_candidates,
    )

    if edge_info:
        return edge_info["to"]

    state_id = current_state_name.replace(".html", "")
    clean_target = normalize_xpath(target_xpath)
    print(f"⚠️ JSON geçiş bulunamadı: state={state_id}, xpath={clean_target}")

    return None


def should_stop_after_edge(edge_info, current_state):
    """
    Do not recursively descend when result.json target was missing and resolved
    back to the same DOM. This prevents state5 -> missing state15 -> state5 loops.
    The generated scenario is still useful because inputValues + click are replayed.
    """
    if not edge_info:
        return False

    if edge_info.get("target_self_loop_due_to_missing"):
        return True

    # General guard: a click edge that points to exactly the same DOM should not
    # create a deeper recursive call. The DOM skeleton would be identical, so the
    # LLM tends to repeat the same action chain.
    return edge_info.get("to") == current_state


def parse_llm_response(response_text):
    if not response_text:
        return None

    if isinstance(response_text, dict):
        return response_text

    resp_str = str(response_text).strip()
    resp_str = resp_str.replace("```json", "").replace("```", "").strip()

    start = resp_str.find("{")
    end = resp_str.rfind("}")

    if start == -1 or end == -1:
        return None

    json_str = resp_str[start:end + 1]

    try:
        return json.loads(json_str)
    except Exception:
        try:
            return ast.literal_eval(json_str)
        except Exception:
            return None


def infer_generic_page_type(elements):
    tags = [normalize_text(e.get("tag", "")) for e in elements]
    blob = " ".join(
        normalize_text(e.get("text", "")) + " " +
        normalize_text(e.get("placeholder", "")) + " " +
        normalize_text(e.get("label_text", "")) + " " +
        normalize_text(e.get("title_attr", "")) + " " +
        normalize_text(e.get("aria_label", ""))
        for e in elements
    )

    input_count = sum(1 for t in tags if t in {"input", "textarea", "select"})
    button_count = sum(1 for t in tags if t == "button")
    link_count = sum(1 for t in tags if t == "a")

    if input_count >= 2 and button_count >= 1:
        return "FORM_OR_WIZARD_PAGE"

    if any(k in blob for k in ["modal", "dialog", "close"]):
        return "POSSIBLE_MODAL_OR_DIALOG"

    if any(k in blob for k in ["edit", "delete", "share", "transaction", "details", "overview"]):
        return "DATA_DETAIL_OR_CRUD_PAGE"

    if link_count > button_count and input_count == 0:
        return "NAVIGATION_OR_LANDING_PAGE"

    return "UNKNOWN_PAGE_TYPE"


def summarize_form_context(elements):
    fields = []
    submits = []

    for el in elements:
        tag = normalize_text(el.get("tag", ""))
        text = el.get("text", "")
        placeholder = el.get("placeholder", "")
        label = el.get("label_text", "")
        typ = el.get("type_attr", "")

        if tag in {"input", "textarea", "select"}:
            fields.append({
                "id": el.get("llm_id"),
                "tag": tag,
                "type": typ,
                "text": text,
                "label": label,
                "placeholder": placeholder,
                "options": el.get("options", [])[:8],
            })

        if tag in {"button", "a", "input"}:
            visible = normalize_text(" ".join([text, label, placeholder, el.get("title_attr", ""), el.get("aria_label", "")]))
            if any(k in visible for k in ["save", "create", "submit", "done", "add", "send", "login", "register"]):
                submits.append({
                    "id": el.get("llm_id"),
                    "tag": tag,
                    "text": text,
                    "label": label,
                    "placeholder": placeholder,
                })

    return {
        "fields": fields,
        "submit_like_controls": submits
    }


def get_state_memory(state_name):
    if state_name not in state_action_memory:
        state_action_memory[state_name] = []

    if state_name not in state_feature_memory:
        state_feature_memory[state_name] = set()

    return state_action_memory[state_name], state_feature_memory[state_name]


def get_dead_end_clicks(state_name):
    if state_name not in dead_end_click_memory:
        dead_end_click_memory[state_name] = set()
    return dead_end_click_memory[state_name]


def remember_dead_end_click(state_name, xpath, text=""):
    dead_key = (
        normalize_xpath(xpath),
        normalize_text(text),
    )
    get_dead_end_clicks(state_name).add(dead_key)


def is_known_dead_end_click(state_name, xpath, text=""):
    dead_key = (
        normalize_xpath(xpath),
        normalize_text(text),
    )
    return dead_key in get_dead_end_clicks(state_name)


def remember_same_state_continue(state_name, xpath, text=""):
    key = (
        normalize_xpath(xpath),
        normalize_text(text),
    )
    if state_name not in same_state_continue_memory:
        same_state_continue_memory[state_name] = {}
    same_state_continue_memory[state_name][key] = same_state_continue_memory[state_name].get(key, 0) + 1
    return same_state_continue_memory[state_name][key]


def infer_feature_signature(step):
    action = normalize_text(step.get("action", ""))
    element = normalize_text(step.get("element", ""))
    tag = normalize_text(step.get("tag", ""))
    type_attr = normalize_text(step.get("type_attr", ""))
    xpath = normalize_xpath(step.get("xpath", ""))
    value = normalize_text(step.get("input_value", ""))

    if action == "input":
        return f"input:{element or xpath}:{value[:20]}"

    if action == "select":
        return f"select:{element or xpath}:{value[:20]}"

    if action == "click":
        # Link olan Register / Sign In / Sign Up form/modal açabilir; commit değildir.
        if tag == "a" and any(k in element for k in [
            "register",
            "sign in",
            "sign up",
            "login",
            "create new",
            "new event",
        ]):
            return f"add_or_open:{element or xpath}"

        # input type=submit gerçek commit'tir.
        if tag == "input" and type_attr in {"submit", "button"}:
            return f"commit:{element or xpath}"

        if (
            "create new" in element
            or "new event" in element
            or element.strip() in {"new", "new event"}
        ):
            return f"add_or_open:{element or xpath}"

        if has_keyword(element, [
            "save",
            "submit",
            "done",
            "register",
            "sign up",
            "sign in",
            "login",
        ]):
            return f"commit:{element or xpath}"

        if element.strip() == "create":
            return f"commit:{element or xpath}"

        if has_keyword(element, ["add", "new"]) or "+" in element:
            return f"add_or_open:{element or xpath}"

        if any(k in element for k in [
            "edit", "delete", "remove", "update",
            "share", "transaction", "details", "overview",
            "owner", "pet", "visit", "veterinarian", "specialty", "type",
            "board", "card", "list",
            "wallet", "goal", "filter", "import",
            "session", "note", "retro",
        ]):
            return f"crud_or_detail:{element or xpath}"

        return f"click:{tag}:{element or xpath}"

    return f"{action}:{element or xpath}"

def is_negative_scenario(reason):
    r = normalize_text(reason)
    return any(k in r for k in [
        "negative",
        "validation",
        "invalid",
        "empty",
        "incomplete",
        "error",
        "destructive",
    ])


def is_commit_like_click(el, action):
    if action != "click":
        return False

    tag = normalize_text(el.get("tag", ""))
    type_attr = normalize_text(el.get("type_attr", ""))

    text = normalize_text(
        " ".join([
            el.get("text", ""),
            el.get("title_attr", ""),
            el.get("aria_label", ""),
            el.get("placeholder", ""),
            el.get("label_text", ""),
            el.get("id_attr", ""),
            el.get("name_attr", ""),
        ])
    )

    # Register / Sign In linkleri genelde form/modal açar; submit değildir.
    if tag == "a" and any(k in text for k in [
        "register",
        "sign in",
        "sign up",
        "login",
        "create new",
        "new event",
    ]):
        return False

    if tag in {"button", "input"}:
        return (
            type_attr == "submit"
            or any(k in text for k in [
                "save",
                "create",
                "submit",
                "done",
                "register",
                "sign up",
                "sign in",
                "login",
            ])
        )

    return False


def apply_configured_input_override(element, input_value):
    """
    Apply app-specific field constraints without replacing the LLM planner.
    Petclinic telephone accepts digits only.
    """
    overrides = config.get("input_overrides", {}) or {}

    field_keys = [
        normalize_text(element.get("id_attr", "")),
        normalize_text(element.get("name_attr", "")),
        normalize_text(element.get("label_text", "")),
        normalize_text(element.get("placeholder", "")),
    ]

    for field_key in field_keys:
        if field_key and field_key in overrides:
            return str(overrides[field_key])

    telephone_like = any(
        "telephone" in field_key or "phone" in field_key
        for field_key in field_keys
        if field_key
    )

    if APP_NAME == "petclinic" and telephone_like:
        digits = re.sub(r"\D", "", str(input_value or ""))
        return digits if len(digits) >= 7 else "5551234567"

    return str(input_value or "")


def get_required_like_field_ids(elements):
    required_ids = []

    for el in elements:
        tag = normalize_text(el.get("tag", ""))
        type_attr = normalize_text(el.get("type_attr", ""))

        if tag not in {"input", "textarea", "select"}:
            continue

        # Submit/button/hidden field doldurulacak alan değildir.
        if type_attr in {"hidden", "submit", "button", "reset", "checkbox", "radio"}:
            continue

        combined = normalize_text(
            " ".join([
                el.get("text", ""),
                el.get("placeholder", ""),
                el.get("label_text", ""),
                el.get("name_attr", ""),
                el.get("id_attr", ""),
                type_attr,
            ])
        )

        if any(k in combined for k in ["optional", "search", "filter"]):
            continue

        required_ids.append(el.get("llm_id"))

    return required_ids


def filled_field_ids_in_path(path, current_state):
    """
    llm_id values restart from el_0 on every DOM state. Count a field as filled
    only when it was filled in the current state; otherwise Owner fields can
    incorrectly satisfy Pet/Vet form validation.
    """
    filled = set()

    for step in path:
        if step.get("state") != current_state:
            continue

        if (
            step.get("action") in {"input", "select"}
            and normalize_text(step.get("input_value", ""))
        ):
            sid = step.get("selected_id")
            if sid:
                filled.add(sid)

    return filled
def build_stable_xpath(el, proposed_xpath=None):
    if proposed_xpath and not looks_invalid_xpath(proposed_xpath):
        return proposed_xpath

    tag = (el.get("tag") or "*").upper()
    text = (el.get("text") or "").strip()
    title = (el.get("title_attr") or "").strip()
    placeholder = (el.get("placeholder") or "").strip()
    aria = (el.get("aria_label") or "").strip()
    name_attr = (el.get("name_attr") or "").strip()
    el_id = (el.get("id_attr") or "").strip()

    if el_id and not el_id.lower().startswith("ember") and not el_id.lower().startswith("el_"):
        return f"//{tag}[@id = {xpath_literal(el_id)}]"

    if name_attr:
        return f"//{tag}[@name = {xpath_literal(name_attr)}]"

    if placeholder:
        return f"//{tag}[@placeholder = {xpath_literal(placeholder)}]"

    if title:
        return f"//{tag}[@title = {xpath_literal(title)}]"

    if aria:
        return f"//{tag}[@aria-label = {xpath_literal(aria)}]"

    if text and len(text) <= 80:
        return f"//{tag}[contains(normalize-space(.), {xpath_literal(text)})]"

    raw = el.get("xpath", "")
    if not looks_invalid_xpath(raw):
        return raw

    return ""


def should_continue_same_state(action_name):
    return action_name in {"input", "select"}

def has_keyword(text, keywords):
    text = normalize_text(text)

    for keyword in keywords:
        keyword = normalize_text(keyword)

        if not keyword:
            continue

        # Çok kelimeli ifadelerde normal contains yeterli.
        if " " in keyword:
            if keyword in text:
                return True
            continue

        # Tek kelimelerde substring değil, token eşleşmesi yap.
        if re.search(rf"(?<![a-z0-9]){re.escape(keyword)}(?![a-z0-9])", text):
            return True

    return False

def is_same_state_exploration_click(text):
    t = normalize_text(text)
    high_value = [
        "save", "create", "submit", "done", "add", "new",
        "edit", "delete", "remove", "update",
        "share", "transaction", "details", "overview",
        "wallet", "goal", "filter", "import",
        "owner", "pet", "visit", "veterinarian", "specialty", "type",
        "board", "card", "list",
        "session", "note", "retro",
        "search", "next", "continue", "+"
    ]
    return any(k in t for k in high_value)


def add_scenario_if_new(path):
    if not path:
        return

    cleaned = []

    for step in path:
        xp = (step.get("xpath") or "").strip()
        if looks_invalid_xpath(xp):
            continue
        cleaned.append(step)

    if not cleaned:
        return

    def canonical_signature_value(step):
        # Input values generated from result.json or LLM often differ only by random text.
        # For duplicate detection, the behavior is the field/action, not the random string.
        if step.get("action") in {"input", "select"}:
            return "<value>"
        return normalize_text(step.get("input_value", ""))

    signature = tuple(
        (
            s.get("action", ""),
            normalize_xpath(s.get("xpath", "")),
            canonical_signature_value(s),
        )
        for s in cleaned
    )

    if signature not in seen_scenario_signatures:
        seen_scenario_signatures.add(signature)
        all_test_scenarios.append(cleaned)


def is_recorded_commit_edge(edge):
    """
    Identify a result.json form-submit edge.

    Petclinic reuses labels such as Add Owner/Add Visit for both an opening
    control and a form submit. Opening edges have no inputValues; submit edges
    carry input steps. Save/Update/Create remain commits by label.
    """
    if not edge:
        return False

    text = normalize_text(
        edge.get("raw_text", "") or edge.get("text", "")
    )

    always_commit = {
        "save",
        "update",
        "submit",
        "create",
        "register",
        "sign up",
        "save pet",
        "update pet",
        "save vet",
        "update vet",
        "save card",
        "save comment",
        "save list",
        "create board",
        "update owner",
        "update visit",
    }

    conditional_add_commits = {
        "add",
        "add owner",
        "add visit",
        "add vet",
    }

    if text in always_commit:
        return True

    if text in conditional_add_commits:
        return bool(edge.get("input_steps"))

    return False


def state_has_recorded_commit_edge(current_state):
    state_id = current_state.replace(".html", "")
    return any(
        is_recorded_commit_edge(edge)
        for edge in STATE_EDGES.get(state_id, [])
    )


def find_recorded_edge_for_element(current_state, element):
    """
    Match a DOM element to an outgoing result.json edge.

    Exact XPath remains the strongest signal. Exact visible text is the
    fallback for semantic parser XPaths such as //BUTTON[contains(...,'Save')]
    versus result.json form-relative XPaths.
    """
    state_id = current_state.replace(".html", "")

    element_xpaths = {
        normalize_xpath(element.get("xpath", "")),
        normalize_xpath(element.get("absolute_xpath", "")),
    }
    element_xpaths.discard("")

    element_text = normalize_text(
        element.get("text", "")
        or element.get("label_text", "")
        or element.get("aria_label", "")
        or element.get("title_attr", "")
        or element.get("value_attr", "")
    )

    xpath_matches = []
    text_matches = []

    for edge in STATE_EDGES.get(state_id, []):
        edge_xpath = normalize_xpath(
            edge.get("raw_xpath", "")
            or edge.get("xpath", "")
        )

        if edge_xpath and edge_xpath in element_xpaths:
            xpath_matches.append(edge)
            continue

        edge_text = normalize_text(
            edge.get("raw_text", "")
            or edge.get("text", "")
        )

        if element_text and edge_text and element_text == edge_text:
            text_matches.append(edge)

    if xpath_matches:
        return xpath_matches[0]

    # Exact text is safe when it identifies a single outgoing edge.
    if len(text_matches) == 1:
        return text_matches[0]

    return None


def annotate_elements_for_hybrid_exploration(
    current_state,
    elements,
    preferred_xpaths,
):
    """
    Keep all DOM elements visible to the LLM.

    Elements are annotated as:
    - recorded and still untried;
    - recorded but already explored;
    - DOM-only discovery candidates.
    """
    untried_count = 0
    explored_count = 0
    discovery_count = 0

    for element in elements:
        candidate_xpaths = {
            normalize_xpath(
                element.get("xpath", "")
            ),
            normalize_xpath(
                element.get(
                    "absolute_xpath",
                    "",
                )
            ),
        }
        candidate_xpaths.discard("")

        matched_edge = find_recorded_edge_for_element(
            current_state,
            element,
        )

        is_untried_recorded = bool(
            candidate_xpaths.intersection(
                preferred_xpaths
            )
        )

        if (
            matched_edge
            and not is_transition_explored(
                matched_edge
            )
        ):
            is_untried_recorded = True

        is_explored_recorded = bool(
            matched_edge
            and is_transition_explored(
                matched_edge
            )
        )

        is_dom_discovery = (
            not is_untried_recorded
            and not is_explored_recorded
        )

        element[
            "crawl_transition_candidate"
        ] = is_untried_recorded

        element[
            "recorded_transition_explored"
        ] = is_explored_recorded

        element[
            "dom_discovery_candidate"
        ] = is_dom_discovery

        if matched_edge:
            element["matched_edge_text"] = (
                matched_edge.get(
                    "raw_text",
                    "",
                )
            )

        if is_untried_recorded:
            untried_count += 1
        elif is_explored_recorded:
            explored_count += 1
        else:
            discovery_count += 1

    # Elementler silinmediği için mevcut llm_id değerlerini
    # korumak daha güvenlidir. Yine de eksik ID varsa üret.
    for index, element in enumerate(elements):
        if not element.get("llm_id"):
            element["llm_id"] = f"el_{index}"

    print(
        "🧭 Hybrid graph guidance: "
        f"untried_json={untried_count}, "
        f"explored_json={explored_count}, "
        f"dom_discovery={discovery_count}"
    )

    return elements

def ensure_unique_recorded_commit_action(
    current_state,
    elements,
    actions_list,
):
    """
    Complete a form plan when the Worker fills fields but omits the only
    recorded submit action. The click is still selected from result.json and
    the current DOM; no Petclinic-specific button text is hard-coded.
    """
    state_id = current_state.replace(".html", "")
    commit_edges = [
        edge
        for edge in STATE_EDGES.get(state_id, [])
        if is_recorded_commit_edge(edge)
    ]

    if len(commit_edges) != 1:
        return actions_list

    commit_edge = commit_edges[0]

    for action in actions_list:
        if normalize_text(action.get("action", "")) != "click":
            continue

        selected = next(
            (
                element
                for element in elements
                if element.get("llm_id") == action.get("selected_id")
            ),
            None,
        )

        if selected and find_recorded_edge_for_element(
            current_state,
            selected,
        ) == commit_edge:
            return actions_list

    has_form_action = any(
        normalize_text(action.get("action", ""))
        in {"input", "select"}
        for action in actions_list
    )

    if not has_form_action:
        return actions_list

    commit_element = next(
        (
            element
            for element in elements
            if find_recorded_edge_for_element(
                current_state,
                element,
            ) == commit_edge
        ),
        None,
    )

    if not commit_element:
        return actions_list

    completed = list(actions_list)
    completed.append({
        "selected_id": commit_element.get("llm_id"),
        "action": "click",
        "input_value": "",
        "robust_xpath": (
            commit_element.get("xpath")
            or commit_element.get("absolute_xpath")
            or commit_edge.get("raw_xpath", "")
        ),
    })

    print(
        "🧩 Tek kayıtlı form submit aksiyonu LLM planına eklendi: "
        f"state={state_id}, text={commit_edge.get('raw_text', '')!r}"
    )

    return completed


def build_context_prompt(
    current_state,
    depth,
    elements,
    current_path,
    preferred_xpaths,
    exploration_mode,
    unexplored_recorded_edges,
):
    page_type = infer_generic_page_type(elements)
    form_summary = summarize_form_context(elements)
    state_actions, state_features = get_state_memory(current_state)

    prompt = generate_worker_prompt(
        current_state,
        elements,
        memory_visited_ids[current_state],
        preferred_xpaths=preferred_xpaths
    )

    prompt += "\n\nMENTOR_CONTEXT:\n"
    prompt += f"- current_depth: {depth}\n"
    prompt += f"- inferred_page_type: {page_type}\n"
    prompt += f"- previous_actions_in_this_state: {state_actions[-10:]}\n"
    prompt += f"- previous_feature_signatures_in_this_state: {sorted(list(state_features))[-15:]}\n"
    prompt += f"- current_test_path_length: {len(current_path)}\n"
    prompt += f"- current_test_path_recent_steps: {current_path[-6:]}\n"
    prompt += f"- form_summary: {json.dumps(form_summary, ensure_ascii=False)}\n"
    prompt += f"- result_json_outgoing_edges: {json.dumps(summarize_outgoing_edges_for_prompt(current_state), ensure_ascii=False)}\n"
    prompt += (
        f"- exploration_mode: "
        f"{exploration_mode}\n"
    )

    prompt += (
            "- unexplored_result_json_edges: "
            + json.dumps(
        [
            {
                "text": edge.get(
                    "raw_text",
                    "",
                ),
                "xpath": edge.get(
                    "raw_xpath",
                    "",
                ),
                "to": edge.get(
                    "to",
                    "",
                ),
            }
            for edge
            in unexplored_recorded_edges
        ],
        ensure_ascii=False,
    )
            + "\n"
    )
    app_hints = config.get("app_hints", [])
    if app_hints:
        prompt += "\nAPP_HINTS:\n"
        for hint in app_hints:
            prompt += f"- {hint}\n"
    prompt += """
    HYBRID_EXPLORATION_POLICY:

    - exploration_mode controls action priority.

    - When exploration_mode is GRAPH_FIRST:
      1. Prefer an untried element marked
         [RECORDED_UNTRIED].
      2. Complete visible form inputs/selects that are
         required for that recorded transition.
      3. Use only one state-changing click in the response.
      4. Do not choose a DOM-only discovery click while an
         untried recorded transition remains.
      5. DOM discovery candidates remain visible for context,
         but they are deferred until recorded edges are exhausted.

    - When exploration_mode is DOM_FALLBACK:
      1. All known result.json transitions for this state have
         already been explored.
      2. Select an untried, meaningful
         [DOM_DISCOVERY_CANDIDATE].
      3. Prefer business actions such as Add, Create, Save,
         Edit, Update, Delete, Details, Search, Filter, tab,
         dropdown or modal controls.
      4. For forms, fill the visible required fields and use
         the visible submit control last.
      5. Use only one state-changing click per response.
      6. Try negative tests.

    - Elements marked [RECORDED_EXPLORED] have already been
      tested. Do not choose them unless they are necessary as
      a prerequisite for reaching a new business action.

    - Return BACKTRACK only when:
      1. no untried recorded transition remains, and
      2. no meaningful untried DOM discovery action remains.
    """


    return prompt


def recursive_explore(current_state, current_path, depth, max_depth=30):
    if depth > max_depth:
        add_scenario_if_new(current_path.copy())
        return

    doms_folder = os.path.join(BASE_CRAWL_DIR, "doms")
    current_dom_path = os.path.join(doms_folder, current_state)

    if not os.path.exists(current_dom_path):
        missing_state_id = current_state.replace(".html", "")
        print(f"⚠️ DOM bulunamadı: {current_dom_path}")

        # If result.json redirected this state to a near-duplicate existing DOM,
        # use that resolved DOM. This avoids dead-ending on pruned Crawljax states.
        resolved_state, ok, reason = resolve_existing_target_state(
            missing_state_id,
            {},
            AVAILABLE_DOM_STATES,
        )

        if ok and resolved_state != current_state:
            print(f"↪️ Eksik DOM için yakın mevcut DOM'a yönlendirildi: {current_state} -> {resolved_state} ({reason})")
            recursive_explore(resolved_state, current_path, depth, max_depth)
            return

        print("🏁 Eksik DOM nedeniyle bu dal burada yaprak senaryo olarak kaydediliyor.")
        add_scenario_if_new(current_path.copy())
        return

    if current_state not in memory_visited_ids:
        memory_visited_ids[current_state] = []

    local_iterations = 0
    max_local_iterations = 16

    while local_iterations < max_local_iterations:
        local_iterations += 1

        print(f"\n[{depth}. DERİNLİK] Mentor: '{current_state}' inceleniyor...")

        elements = extract_actionable_skeleton(current_dom_path)

        # App-specific ignored elements are removed before the Worker LLM sees them.
        elements_before_filter = len(elements)
        elements = [
            el for el in elements
            if not should_ignore_element(el, config)
        ]

        ignored_count = elements_before_filter - len(elements)
        if ignored_count:
            print(f"🚫 App ignore filter: {ignored_count} element LLM promptundan çıkarıldı.")

        if not elements:
            print("⚠️ Bu sayfada etkileşebilir element bulunamadı.")
            add_scenario_if_new(current_path.copy())
            break

        state_id = current_state.replace(".html", "")
        unexplored_recorded_edges = (
            get_unexplored_recorded_edges(
                current_state
            )
        )

        exploration_mode = (
            "GRAPH_FIRST"
            if unexplored_recorded_edges
            else "DOM_FALLBACK"
        )

        # Sadece henüz denenmemiş edge XPath'leri
        # yüksek öncelikli olarak gönderilir.
        preferred_xpaths = {
            normalize_xpath(
                edge.get("xpath", "")
                or edge.get("raw_xpath", "")
            )
            for edge in unexplored_recorded_edges
        }

        preferred_xpaths.discard("")

        elements = (
            annotate_elements_for_hybrid_exploration(
                current_state,
                elements,
                preferred_xpaths,
            )
        )

        print(
            f"🧭 Exploration mode: "
            f"{exploration_mode} | "
            f"unexplored_result_edges="
            f"{len(unexplored_recorded_edges)}"
        )

        prompt = build_context_prompt(
            current_state=current_state,
            depth=depth,
            elements=elements,
            current_path=current_path,
            preferred_xpaths=preferred_xpaths,
            exploration_mode=exploration_mode,
            unexplored_recorded_edges=(
                unexplored_recorded_edges
            ),
        )

        decision = None

        for attempt in range(2):
            llm_text = ask_worker_llm(
                prompt,
                elements=elements,
                visited_ids=memory_visited_ids[current_state],
                preferred_xpaths=preferred_xpaths,
                max_actions=15
            )

            parsed = parse_llm_response(llm_text)
            decision = validate_llm_decision(parsed, elements) if parsed else None

            if decision:
                break

            print(f"⚠️ Parse/validate başarısız (deneme {attempt + 1}): {repr(llm_text)}")

        if not decision:
            print("⚠️ LLM çözülemedi; fallback kapalı. BACKTRACK yapılıyor.")
            decision = {
                "scenario_reason": "LLM decision could not be parsed or validated; fallback is disabled, so backtracking.",
                "actions": [
                    {
                        "selected_id": "NONE",
                        "action": "BACKTRACK",
                        "input_value": "",
                        "robust_xpath": ""
                    }
                ]
            }

        if exploration_mode == "GRAPH_FIRST":
            actions_list = (
                ensure_unique_recorded_commit_action(
                    current_state,
                    elements,
                    decision.get("actions", []),
                )
            )
        else:
            actions_list = decision.get(
                "actions",
                [],
            )
        if not actions_list:
            print("⚠️ Action list boş, geri dönülüyor.")
            add_scenario_if_new(current_path.copy())
            break

        first_action = actions_list[0]
        if first_action.get("selected_id") == "NONE" or first_action.get("action") == "BACKTRACK":
            print(f"🔙 Worker LLM backtrack istedi: {decision.get('scenario_reason', '')}")
            add_scenario_if_new(current_path.copy())
            break

        scenario_reason = decision.get("scenario_reason", "")

        print(f"🧠 LLM Senaryo: {scenario_reason or 'Belirtilmedi'}")

        temp_path = current_path.copy()
        applied_any_action = False
        last_xpath = None
        last_text = ""
        last_action = ""
        last_feature = ""
        last_edge_info = None
        dom_only_leaf = False

        seen_action_keys_in_response = set()

        for act in actions_list:
            sid = act.get("selected_id")
            el = next((e for e in elements if e["llm_id"] == sid), None)

            if not el:
                print(f"⚠️ Element bulunamadı: {sid}")
                continue

            if should_ignore_element(el, config):
                print(f"🚫 Ignore edilen element atlandı: {el.get('text', '') or el.get('href', '') or sid}")
                continue

            action = normalize_text(act.get("action", "click"))
            stable_xpath = build_stable_xpath(el, act.get("robust_xpath", ""))

            if not stable_xpath:
                print(f"⚠️ Geçersiz locator atlandı: {el.get('text', '')}")
                continue

            input_value = act.get("input_value", "") or ""

            if action in {"input", "select"}:
                input_value = apply_configured_input_override(
                    el,
                    input_value,
                )

            # One response must not enter six different values into the same
            # field. Different values are alternative scenarios, not one plan.
            action_key = (
                action,
                normalize_xpath(stable_xpath),
            )

            if action_key in seen_action_keys_in_response:
                print(f"⚠️ Aynı LLM cevabı içinde tekrar eden aksiyon atlandı: {action.upper()} {stable_xpath}")
                continue

            seen_action_keys_in_response.add(action_key)

            visible_for_dead_end = normalize_text(
                " ".join([
                    el.get("text", ""),
                    el.get("title_attr", ""),
                    el.get("aria_label", ""),
                    el.get("placeholder", ""),
                    el.get("label_text", ""),
                    el.get("id_attr", ""),
                    el.get("name_attr", ""),
                ])
            )

            if (
                exploration_mode
                == "DOM_FALLBACK"
                and (
                    current_state,
                    normalize_xpath(
                        stable_xpath
                    ),
                )
                in dom_only_attempted_actions
            ):
                print(
                    "⚠️ DOM-only action daha önce "
                    "leaf olarak denendi."
                )
                continue
            # Aynı element tekrar seçilmişse sadece aynı state içinde gereksiz tekrarları azalt.
            if sid in memory_visited_ids[current_state] and action == "click":
                visible = normalize_text(
                    " ".join([
                        el.get("text", ""),
                        el.get("title_attr", ""),
                        el.get("aria_label", ""),
                        el.get("placeholder", ""),
                        el.get("label_text", ""),
                        el.get("id_attr", ""),
                        el.get("name_attr", ""),
                        el.get("class_attr", ""),
                        el.get("role", ""),
                    ])
                )

                repeat_allowed_keywords = [
                    "save", "create", "submit", "done", "add", "new", "+",
                    "edit", "delete", "remove", "update",
                    "details", "transaction", "wallet", "goal", "filter", "import",
                    "owner", "pet", "visit", "veterinarian", "specialty", "type",
                    "board", "card", "list",
                    "session", "note", "retro",
                    "next", "continue",
                ]

                if not any(k in visible for k in repeat_allowed_keywords):
                    print(f"⚠️ Bu click elementi zaten denenmiş: {sid}")
                    continue

            step = {
                "selected_id": sid,
                "element": el.get("text", "") or el.get("placeholder", "") or el.get("label_text", ""),
                "tag": el.get("tag", ""),
                "type_attr": el.get("type_attr", ""),
                "xpath": stable_xpath,
                "action": action,
                "input_value": input_value,
                "options": el.get("options", []),
                "state": current_state,
                "scenario_reason": scenario_reason,
            }

            edge_info = None
            if action == "click":
                edge_info = find_transition_edge(
                    current_state,
                    stable_xpath,
                    chosen_text=step["element"],
                    extra_xpath_candidates=[
                        el.get("xpath", ""),
                        el.get("absolute_xpath", ""),
                    ],
                )
                if edge_info and is_transition_explored(edge_info):
                    print(
                        f"⚠️ Bu transition daha önce explored edildi, tekrar atlandı: "
                        f"{edge_info.get('from')} -> {edge_info.get('to')} | {edge_info.get('raw_text')}"
                    )
                    continue

                if (
                        exploration_mode == "GRAPH_FIRST"
                        and action == "click"
                        and not edge_info
                ):
                    print(
                        "🧭 DOM-discovery click ertelendi; "
                        "önce result.json edge'leri tüketilecek: "
                        f"state={state_id}, "
                        f"text={step['element']!r}, "
                        f"xpath={stable_xpath}, "
                        f"remaining_edges="
                        f"{len(unexplored_recorded_edges)}"
                    )
                    continue

                if edge_info and edge_info.get("input_steps"):
                    replay_steps = edge_replay_steps(
                        edge_info,
                        current_state=current_state,
                        elements=elements,
                        scenario_reason=scenario_reason,
                    )
                    append_unique_replay_steps(temp_path, replay_steps)

            # Happy-path senaryoda required-like alanlar dolmadan Sign Up/Register/Save/Create/Submit tıklanmasın.
            commit_like = (
                is_commit_like_click(el, action)
                or (
                    action == "click"
                    and is_recorded_commit_edge(edge_info)
                )
            )

            if commit_like and not is_negative_scenario(scenario_reason):
                required_ids = set(get_required_like_field_ids(elements))
                filled_ids = filled_field_ids_in_path(
                    temp_path,
                    current_state,
                )

                missing_ids = sorted(list(required_ids - filled_ids))

                if missing_ids:
                    print(
                        f"⏭️ Commit/Register/Save ertelendi; happy-path için eksik alanlar var: {missing_ids}"
                    )
                    continue
            if (
                action == "click"
                and is_recorded_commit_edge(edge_info)
            ):
                feature = (
                    "commit:"
                    + normalize_text(
                        edge_info.get("raw_text", "")
                        or step.get("element", "")
                        or stable_xpath
                    )
                )
            else:
                feature = infer_feature_signature(step)

            step["feature_signature"] = feature

            memory_visited_ids[current_state].append(sid)
            state_actions, state_features = get_state_memory(current_state)
            state_actions.append({
                "id": sid,
                "action": action,
                "element": step["element"],
                "feature": feature,
            })
            state_features.add(feature)

            temp_path.append(step)
            applied_any_action = True
            if (
                action == "click"
                and exploration_mode == "DOM_FALLBACK"
                and not edge_info
            ):
                dom_only_attempted_actions.add(
                    (
                        current_state,
                        normalize_xpath(
                            stable_xpath
                        ),
                    )
                )

                print(
                    "🧪 DOM-only LLM aksiyonu leaf olarak "
                    "kaydedilecek: "
                    f"state={current_state}, "
                    f"text={step.get('element', '')!r}, "
                    f"xpath={stable_xpath}"
                )

                dom_only_leaf = True
            last_xpath = stable_xpath
            last_text = step["element"]
            last_action = action
            last_feature = feature
            last_edge_info = edge_info
            
            if dom_only_leaf:
                break

            if action == "click" and edge_info and edge_info.get("to"):
                remember_transition_explored(edge_info)

                if not should_stop_after_edge(edge_info, current_state):
                    print(
                        f"⏩ State-changing edge bulundu; kalan LLM aksiyonları atlanacak. "
                        f"{current_state} -> {edge_info.get('to')}"
                    )
                    break
            print(
                f"   ✅ LLM Adım: {action.upper()} -> {step['element']} | "
                f"{stable_xpath} | value={repr(input_value)} | feature={feature}"
            )

        if dom_only_leaf:
            add_scenario_if_new(
                temp_path.copy()
            )

            print(
                "🏁 DOM-only aksiyon JSON state'i "
                "bilinmediği için dal burada bitirildi."
            )

            break
        if not applied_any_action:
            print("⚠️ Uygulanabilir yeni LLM aksiyonu kalmadı.")
            add_scenario_if_new(temp_path.copy())
            break

        if should_continue_same_state(last_action):
            print("↪️ Input/Select sonrası aynı state üzerinde LLM ile devam ediliyor.")
            current_path[:] = temp_path
            continue

        if last_edge_info:
            next_st = last_edge_info["to"]
        else:
            next_st = find_next_state(current_state, last_xpath, last_text)

        if next_st:
            if last_edge_info and should_stop_after_edge(last_edge_info, current_state):
                print(
                    "🏁 Edge replay edildi ancak hedef aynı DOM'a çözüldü; "
                    f"recursive iniş durduruldu. original={last_edge_info.get('to_original')} "
                    f"resolved={last_edge_info.get('to')} "
                    f"reason={last_edge_info.get('target_resolve_reason')}"
                )
                add_scenario_if_new(temp_path.copy())
                break
            if depth >= max_depth:
                print(
                    f"🏁 Max depth sınırına ulaşıldı: depth={depth}, "
                    f"next={next_st}. Dal yaprak senaryo olarak kaydediliyor."
                )
                add_scenario_if_new(temp_path.copy())
                break
            recursive_explore(next_st, temp_path, depth + 1, max_depth)
        else:
            if is_same_state_exploration_click(last_text):
                retry_count = remember_same_state_continue(current_state, last_xpath, last_text)

                # Allow at most one same-state retry for a high-value click that may open a modal.
                # After that, treat it as a leaf. Repeating Add New Transaction / Add New Event
                # without a result.json transition produces many near-duplicate tests.
                if retry_count <= 1:
                    print("↪️ Yeni state yok; yüksek değerli click için aynı state üzerinde yalnızca 1 kez daha denenecek.")
                    current_path[:] = temp_path
                    continue

                remember_dead_end_click(current_state, last_xpath, last_text)
                print("🏁 Aynı high-value click tekrar yeni JSON state üretmedi; dead-end kabul edilip yaprak senaryo kaydediliyor.")
                add_scenario_if_new(temp_path.copy())
                break

            print("🏁 Test Tamamlandı (Yaprak).")
            add_scenario_if_new(temp_path.copy())

    if local_iterations >= max_local_iterations:
        print(f"⚠️ State iteration limiti doldu: {current_state}")
        add_scenario_if_new(current_path.copy())


if __name__ == "__main__":
    print("\n" + "🚀" * 10 + " LLM-CENTRIC RLM TEST MOTORU BAŞLATILDI " + "🚀" * 10)

    start_state = load_transition_map(BASE_CRAWL_DIR)
    print(f"🚀 Mentor başlangıç state'i: {start_state}")
    recursive_explore(start_state, [], 1)

    print("\n" + "=" * 50)
    print(f"🏆 TOPLAM {len(all_test_scenarios)} LLM-TABANLI SENARYO ÜRETİLDİ!")
    print("=" * 50)




    generate_dante_suites(
        all_test_scenarios,
        app_name=APP_NAME,
        login_config=config.get("login"),
        wait_time=config.get(
            "wait_time",
            1000,
        ),
        start_url=config.get("start_url"),
        output_dir=GENERATED_TEST_DIR,
    )
