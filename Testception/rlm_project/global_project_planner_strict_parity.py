import os
import json
import ast
import sys
import re
from bs4 import BeautifulSoup
from rlm import RLM

from crawljax_parser import extract_actionable_skeleton
from dante_suite_generator import generate_dante_suites

# Strict-parity goal:
# - Keep the latest PROJECT_CONFIGS / app hints style
# - Keep the same parser fields
# - Keep the same mentor guidance philosophy
# - Change ONLY the planning granularity:
#   recursive per-state planning  ->  single global planning over all states


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
            "user_val": "foo@bar.com",
            "pass_xpath": "/HTML[1]/BODY[1]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]",
            "pass_val": "foobar123",
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
        "ignore_xpaths": [],
        "login": {
            "user_xpath": "/HTML[1]/BODY[1]/MAIN[1]/DIV[1]/DIV[1]/MAIN[1]/FORM[1]/DIV[1]/INPUT[1]",
            "user_val": "john@phoenix-trello.com",
            "pass_xpath": "/HTML[1]/BODY[1]/MAIN[1]/DIV[1]/DIV[1]/MAIN[1]/FORM[1]/DIV[2]/INPUT[1]",
            "pass_val": "12345678",
            "submit_type": "click",
            "submit_xpath": "/HTML[1]/BODY[1]/MAIN[1]/DIV[1]/DIV[1]/MAIN[1]/FORM[1]/BUTTON[1]"
        },
        "app_hints": [
            "Phoenix Trello is a board/list/card management app.",
            "Login is configured as a precondition with john@phoenix-trello.com / 12345678. Do not repeatedly test login after login is configured.",
            "If the current state is unauthenticated, valid login and create-new-account flows are meaningful. Otherwise prioritize board/card business flows.",
            "After login, prioritize Boards, New Board, board name form, Create Board, opening boards, lists, cards, card details, edit, delete, move, and modal dialogs.",
            "On the My Boards page, New Board and Board Name are high-value. Fill Board Name before clicking Create Board.",
            "Create new account is a lower-priority alternative flow; if tested, fill First name, Last name, Email, Password, Confirm password before Sign up.",
            "Sign out and icon/profile actions are lower priority than board/list/card CRUD.",
            "For happy-path forms, submit must be the final action after all required-looking fields are filled.",
            "Do not treat the Phoenix header/logo/navbar container as a commit button."
        ]
    },
    "petclinic": {
        "strategy": "Fired",
        "has_crawl0": True,
        "wait_time": 250,
        "ignore_xpaths": [],
        "login": None,
        "app_hints": [
            "Petclinic is a veterinary clinic CRUD app. There is no login precondition.",
            "Prioritize business flows under Owners, Veterinarians, Pet Types, and Specialties.",
            "Important owner flows: Find Owners, Add Owner, fill First Name, Last Name, Address, City, Telephone, then click Add Owner.",
            "For happy-path owner forms, fill all visible required-looking fields before clicking Add Owner or Update Owner.",
            "Important pet flows: add/edit pet, fill pet name, birth date, type, owner-related fields if present, then submit.",
            "Important visit flows: add visit, fill date and description, then submit.",
            "Important veterinarian/specialty/pet-type flows: Add, Edit, Delete, update name/type fields, then submit.",
            "In Specialties and Pet Types pages, Edit and Delete buttons are high-value CRUD actions. Add button is also high-value and should not be ignored.",
            "Home and logo navigation are lower priority after at least one CRUD/form flow is tested.",
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


def load_transition_map(base_crawl_dir):
    transition_map = {}
    state_candidate_xpaths = {}
    state_edges = {}

    json_path = os.path.join(base_crawl_dir, "result.json")
    if not os.path.exists(json_path):
        return transition_map, state_candidate_xpaths, state_edges

    with open(json_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    edges = data.get("edges", [])

    for edge in edges:
        if not isinstance(edge, dict):
            continue

        from_state = edge.get("from")
        to_state = edge.get("to")
        raw_xpath = edge.get("id", "")
        xpath_key = normalize_xpath(raw_xpath)
        text_key = normalize_text(edge.get("text", ""))

        if from_state and to_state and xpath_key:
            transition_map[(from_state, xpath_key)] = f"{to_state}.html"
            state_candidate_xpaths.setdefault(from_state, set()).add(xpath_key)
            state_edges.setdefault(from_state, []).append({
                "to": f"{to_state}.html",
                "xpath": xpath_key,
                "text": text_key,
                "raw_text": edge.get("text", "")
            })

    return transition_map, state_candidate_xpaths, state_edges


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


def collect_global_states(base_crawl_dir):
    doms_folder = os.path.join(base_crawl_dir, "doms")
    if not os.path.isdir(doms_folder):
        raise FileNotFoundError(f"DOM folder not found: {doms_folder}")

    _, state_candidate_xpaths, _ = load_transition_map(base_crawl_dir)
    dom_files = sorted([f for f in os.listdir(doms_folder) if f.endswith(".html")])

    global_elements = []
    states_payload = []

    for state_idx, current_state in enumerate(dom_files):
        current_dom_path = os.path.join(doms_folder, current_state)
        elements = extract_actionable_skeleton(current_dom_path)

        state_id = current_state.replace(".html", "")
        preferred_xpaths = {
            normalize_xpath(xp)
            for xp in state_candidate_xpaths.get(state_id, set())
        }

        local_entries = []
        for el in elements:
            gid = f"s{state_idx}_{el.get('llm_id', '')}"
            enriched = dict(el)
            enriched["global_id"] = gid
            enriched["state_name"] = current_state
            enriched["preferred_transition_candidate"] = normalize_xpath(el.get("xpath", "")) in preferred_xpaths
            global_elements.append(enriched)
            local_entries.append(enriched)

        states_payload.append({
            "state_name": current_state,
            "page_type": infer_generic_page_type(elements),
            "form_summary": summarize_form_context(elements),
            "elements": local_entries
        })

    return global_elements, states_payload


def build_global_parity_prompt(states_payload, app_hints):
    prompt = "GLOBAL_PROJECT_CONTEXT:\n"
    prompt += "You are still the decision maker. The only difference is that you can now see ALL states at once instead of one state at a time.\n"
    prompt += "Generate the best high-value test scenarios across the whole project.\n\n"

    if app_hints:
        prompt += "APP_HINTS:\n"
        for hint in app_hints:
            prompt += f"- {hint}\n"
        prompt += "\n"

    prompt += """MENTOR_GUIDANCE:
- You are the decision maker. The mentor only provides DOM context, memory, transition hints, and app hints.
- Prefer meaningful application behavior over static/header/footer/social links.
- Prioritize actions that can change application state or reveal new UI:
  add, create, edit, update, delete, save, submit, search, filter, import, details, open, expand, dropdown, tab, modal.
- For landing/login/register pages:
  use configured login context when available; do not repeatedly test login/register after the app already has a login precondition.
- For forms:
  negative validation submit is useful once, but do not repeat incomplete submit endlessly.
  after a negative submit, prefer a valid happy-path flow.
  in happy-path flows, fill required-looking inputs/selects before final Save/Create/Add/Submit/Register/Sign Up.
  final commit should usually be the last action in the sequence.
- Treat Add/New/+ carefully:
  if it opens a form/modal/detail panel, click it as an opening action, not as final submit.
  if it is clearly a submit control inside a form, use it as the final commit after required fields are filled.
- For modals/popups:
  interact with visible fields/buttons inside the modal before closing or switching away.
- For data/detail pages:
  prefer business actions such as edit, delete, add item, add transaction, add visit, add card, set goal, filters, tabs, and details.
- Static links such as About, GitHub, Source, Contact, API, Language, Home, footer links are lower priority.
  Test them only after main business/form/CRUD actions are covered or no business action remains.
- Avoid repeating the same feature signature unless it is part of a new valid flow.
- If no meaningful untried business action remains, omit it from scenarios.
"""

    prompt += """
GLOBAL_OUTPUT_TASK:
Return ONLY valid JSON.
Return a project-level scenario list instead of one state-level action list.

Expected format:
{
  "scenario_reason": "Short overall reason",
  "scenarios": [
    {
      "scenario_name": "Short scenario title",
      "scenario_reason": "Why this scenario matters",
      "actions": [
        {
          "selected_id": "s0_el_1",
          "action": "click|input|select",
          "input_value": "",
          "robust_xpath": "//INPUT[@name='...']"
        }
      ]
    }
  ]
}

RULES:
- selected_id must be one of the provided GLOBAL_ID values.
- robust_xpath should use the provided xpath unless you have a more stable equivalent.
- For input/textarea, use action=input and provide a meaningful input_value.
- For select, use action=select and choose a non-default valid option.
- Produce 8 to 20 scenarios when possible.
- Each scenario should be multi-step when the UI allows it.
"""

    prompt += "\n\nGLOBAL_STATE_SKELETON:\n"

    for state in states_payload:
        prompt += f"\n=== STATE: {state['state_name']} ===\n"
        prompt += f"PAGE_TYPE: {state['page_type']}\n"
        prompt += f"FORM_SUMMARY: {json.dumps(state['form_summary'], ensure_ascii=False)}\n"
        prompt += "ACTIONABLE_DOM_SKELETON:\n"

        for el in state["elements"]:
            preferred = "[CRAWL_TRANSITION_CANDIDATE] " if el["preferred_transition_candidate"] else ""
            line = (
                f"- GLOBAL_ID: {el['global_id']} {preferred}"
                f"| state={el['state_name']} "
                f"| local_id={el.get('llm_id', '')} "
                f"| tag={el.get('tag', '')} "
                f"| type={el.get('type_attr', '')} "
                f"| text={repr(el.get('text', ''))} "
                f"| label={repr(el.get('label_text', ''))} "
                f"| placeholder={repr(el.get('placeholder', ''))} "
                f"| id={repr(el.get('id_attr', ''))} "
                f"| name={repr(el.get('name_attr', ''))} "
                f"| title={repr(el.get('title_attr', ''))} "
                f"| aria={repr(el.get('aria_label', ''))} "
                f"| role={repr(el.get('role', ''))} "
                f"| class={repr(el.get('class_attr', ''))} "
                f"| xpath={el.get('xpath', '')}"
            )

            if el.get("tag") == "select" and el.get("options"):
                line += f" | options={el['options']}"

            prompt += line + "\n"

    return prompt


def ask_global_llm_with_single_agent(prompt_text):
    print("🧠 Single global LLM is analyzing the whole project in one pass...")

    agent = RLM(
        backend="litellm",
        backend_kwargs={
            "model_name": "openrouter/openai/gpt-4o-mini",
            "api_key": os.environ.get("OPENROUTER_API_KEY", ""),
            "temperature": 0.2,
        },
        environment="local",
        environment_kwargs={},
        max_depth=1,
        verbose=False,
    )

    result = agent.completion(prompt_text)
    return result.response


def fallback_input_value(el):
    tag = (el.get("tag") or "").lower()
    type_attr = normalize_text(el.get("type_attr", ""))
    placeholder = normalize_text(el.get("placeholder", ""))
    label = normalize_text(el.get("label_text", ""))
    text = normalize_text(el.get("text", ""))
    name_attr = normalize_text(el.get("name_attr", ""))
    combined = " ".join([placeholder, label, text, name_attr])

    if tag == "select":
        options = el.get("options") or []
        usable = [
            opt for opt in options
            if normalize_text(opt) and "select" not in normalize_text(opt) and "choose" not in normalize_text(opt)
        ]
        if len(usable) >= 2:
            return usable[1]
        if usable:
            return usable[0]
        return options[0] if options else ""

    if type_attr == "email" or "email" in combined:
        return "qa.user@example.com"
    if type_attr == "number" or "amount" in combined or "price" in combined or "count" in combined:
        return "42"
    if type_attr == "date" or "date" in combined or "when" in combined:
        return "2026-01-01"
    if "name" in combined:
        return "QA User"
    if "title" in combined or "subject" in combined:
        return "QA Automation Test"
    if "description" in combined or tag == "textarea":
        return "This is a QA-generated exploratory test input with meaningful content."
    return "QA Test Value"


def build_deterministic_fallback(global_elements):
    scenarios = []
    grouped = {}

    for el in global_elements:
        grouped.setdefault(el["state_name"], []).append(el)

    for state_name, elements in grouped.items():
        opens, fills, selects, commits = [], [], [], []

        for el in elements:
            tag = str(el.get("tag", "")).lower()
            text_blob = " ".join([
                str(el.get("text", "")),
                str(el.get("label_text", "")),
                str(el.get("placeholder", "")),
                str(el.get("title_attr", "")),
                str(el.get("aria_label", "")),
                str(el.get("role", "")),
                str(el.get("class_attr", "")),
            ]).lower()

            if tag in {"input", "textarea"}:
                fills.append({
                    "selected_id": el["global_id"],
                    "action": "input",
                    "input_value": fallback_input_value(el),
                    "robust_xpath": el.get("xpath", "")
                })
            elif tag == "select":
                val = fallback_input_value(el)
                if val:
                    selects.append({
                        "selected_id": el["global_id"],
                        "action": "select",
                        "input_value": val,
                        "robust_xpath": el.get("xpath", "")
                    })

            if any(k in text_blob for k in ["create", "new", "add", "+", "start", "open", "let's start"]):
                opens.append({
                    "selected_id": el["global_id"],
                    "action": "click",
                    "input_value": "",
                    "robust_xpath": el.get("xpath", "")
                })

            if any(k in text_blob for k in ["save", "submit", "done", "register", "sign up", "sign in", "login", "create"]):
                commits.append({
                    "selected_id": el["global_id"],
                    "action": "click",
                    "input_value": "",
                    "robust_xpath": el.get("xpath", "")
                })

        steps = []
        if opens:
            steps.append(opens[0])
        steps.extend(fills[:3])
        if selects:
            steps.append(selects[0])
        if commits:
            steps.append(commits[0])

        if steps:
            scenarios.append({
                "scenario_name": f"Fallback scenario for {state_name}",
                "scenario_reason": "Deterministic fallback because the global planner did not return valid JSON.",
                "actions": steps
            })

    return {
        "scenario_reason": "Global fallback planning",
        "scenarios": scenarios[:12]
    }


def flatten_to_dante_scenarios(parsed, global_map):
    final_scenarios = []

    for sc in parsed.get("scenarios", []):
        actions = sc.get("actions", [])
        scenario_reason = sc.get("scenario_reason", "")
        scenario_name = sc.get("scenario_name", "")

        converted = []
        for act in actions:
            gid = act.get("selected_id")
            if gid not in global_map:
                continue

            base = global_map[gid]
            action = str(act.get("action", "click")).strip().lower()
            input_value = str(act.get("input_value", "") or "")
            robust_xpath = str(act.get("robust_xpath", "") or "").strip() or base.get("xpath", "")

            if not robust_xpath:
                continue

            if base.get("tag", "").lower() in {"input", "textarea"}:
                action = "input"
                if not input_value:
                    input_value = fallback_input_value(base)
            elif base.get("tag", "").lower() == "select":
                action = "select"
                if not input_value:
                    input_value = fallback_input_value(base)
            else:
                action = "click"
                input_value = ""

            converted.append({
                "element": scenario_name or base.get("state_name", ""),
                "xpath": robust_xpath,
                "action": action,
                "input_value": input_value,
                "scenario_reason": scenario_reason,
                "tag": base.get("tag", ""),
                "type_attr": base.get("type_attr", ""),
            })

        if converted:
            final_scenarios.append(converted)

    return final_scenarios


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("❌ ERROR: Please provide an application name.")
        print("Usage: python3 global_project_planner_strict_parity.py retroboard")
        sys.exit(1)

    app_name = sys.argv[1].lower()

    if app_name not in PROJECT_CONFIGS:
        print(f"❌ ERROR: Unknown application '{app_name}'.")
        sys.exit(1)

    if "OPENROUTER_API_KEY" not in os.environ:
        print("❌ ERROR: OPENROUTER_API_KEY environment variable not found.")
        sys.exit(1)

    config = PROJECT_CONFIGS[app_name]
    base_path = f"/home/mecit/workspace/TREL26-Submission-Material-Testception/dante/applications/{app_name}/localhost/crawl-with-inputs"
    base_crawl_dir = os.path.join(base_path, "crawl0") if config["has_crawl0"] else base_path

    print("\n" + "=" * 60)
    print(f"🎯 TARGET PROJECT: {app_name.upper()}")
    print(f"📂 WORKING DIRECTORY: {base_crawl_dir}")
    print("🧠 MODE: STRICT-PARITY SINGLE GLOBAL LLM")
    print("=" * 60 + "\n")

    global_elements, states_payload = collect_global_states(base_crawl_dir)
    global_map = {el["global_id"]: el for el in global_elements}

    print(f"📦 Global state collection prepared. Total states: {len(states_payload)}")
    print(f"🧩 Total actionable elements exposed to the global planner: {len(global_elements)}")

    prompt = build_global_parity_prompt(states_payload, config.get("app_hints", []))
    raw_response = ask_global_llm_with_single_agent(prompt)
    parsed = parse_llm_response(raw_response)

    if not parsed or "scenarios" not in parsed:
        print("⚠️ Global planner did not return valid scenario JSON. Using deterministic fallback.")
        print(raw_response)
        parsed = build_deterministic_fallback(global_elements)

    scenarios = flatten_to_dante_scenarios(parsed, global_map)

    print(f"🏆 Total scenarios generated: {len(scenarios)}")

    if not scenarios:
        print("❌ ERROR: No valid scenarios were produced.")
        sys.exit(1)

    generate_dante_suites(
        scenarios,
        app_name=app_name,
        login_config=config.get("login"),
        wait_time=config.get("wait_time", 1000)
    )

    print("✅ Strict-parity global planning completed.")
