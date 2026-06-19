import os
import json
import sys
import ast
import re
from crawljax_parser import extract_actionable_skeleton, generate_worker_prompt
from worker_agent import ask_worker_llm, build_fallback_decision, validate_llm_decision
from dante_suite_generator import generate_dante_suites


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


if len(sys.argv) < 2:
    print("❌ HATA: Lütfen terminalden bir proje adı girin! Örn: python3 mentor_orchestrator.py splittypie")
    sys.exit(1)

APP_NAME = sys.argv[1].lower()

if APP_NAME not in PROJECT_CONFIGS:
    print(f"❌ HATA: '{APP_NAME}' konfigürasyonu bulunamadı.")
    sys.exit(1)

config = PROJECT_CONFIGS[APP_NAME]

REPO_ROOT = "/home/mecit/workspace/TREL26-Submission-Material-Testception"

BASE_CRAWL_DIR = (
    f"/home/mecit/workspace/TREL26-Submission-Material-Testception/"
    f"dante/applications/{APP_NAME}/localhost/crawl-with-inputs/crawl0"
)

TRANSITION_MAP = {}
STATE_CANDIDATE_XPATHS = {}
STATE_EDGES = {}

memory_visited_ids = {}
state_action_memory = {}
state_feature_memory = {}
all_test_scenarios = []
seen_scenario_signatures = set()


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


def load_transition_map(base_crawl_dir):
    global TRANSITION_MAP, STATE_CANDIDATE_XPATHS, STATE_EDGES

    json_path = os.path.join(base_crawl_dir, "result.json")

    if not os.path.exists(json_path):
        print(f"⚠️ HATA: {json_path} bulunamadı!")
        return

    with open(json_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    edges = data.get("edges", [])
    loaded_count = 0

    for edge in edges:
        if not isinstance(edge, dict):
            continue

        from_state = edge.get("from")
        to_state = edge.get("to")
        raw_xpath = edge.get("id", "")
        xpath_key = normalize_xpath(raw_xpath)
        text_key = normalize_text(edge.get("text", ""))

        if from_state and to_state and xpath_key:
            TRANSITION_MAP[(from_state, xpath_key)] = f"{to_state}.html"
            STATE_CANDIDATE_XPATHS.setdefault(from_state, set()).add(xpath_key)
            STATE_EDGES.setdefault(from_state, []).append({
                "to": f"{to_state}.html",
                "xpath": xpath_key,
                "text": text_key,
                "raw_text": edge.get("text", "")
            })
            loaded_count += 1

    print(f"✅ result.json haritası yüklendi: {loaded_count} yol tanımlandı.")


def find_next_state(current_state_name, target_xpath, chosen_text=""):
    state_id = current_state_name.replace(".html", "")
    clean_target = normalize_xpath(target_xpath)
    clean_text = normalize_text(chosen_text)

    next_state = TRANSITION_MAP.get((state_id, clean_target))
    if next_state:
        print(f"➡️ JSON geçiş bulundu: state={state_id}, xpath={clean_target}, next={next_state}")
        return next_state

    print(f"⚠️ JSON geçiş bulunamadı: state={state_id}, xpath={clean_target}")

    edges = STATE_EDGES.get(state_id, [])
    if clean_text:
        for edge in edges:
            edge_text = edge.get("text", "")
            if edge_text and (clean_text == edge_text or clean_text in edge_text or edge_text in clean_text):
                print(f"➡️ TEXT fallback geçiş bulundu: state={state_id}, text={clean_text}, next={edge['to']}")
                return edge["to"]

    return None


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

        if any(k in element for k in [
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

        if any(k in element for k in ["add", "new", "+"]):
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


def filled_field_ids_in_path(path):
    filled = set()

    for step in path:
        if step.get("action") in {"input", "select"} and normalize_text(step.get("input_value", "")):
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

    signature = tuple(
        (
            s.get("action", ""),
            normalize_xpath(s.get("xpath", "")),
            normalize_text(s.get("input_value", "")),
        )
        for s in cleaned
    )

    if signature not in seen_scenario_signatures:
        seen_scenario_signatures.add(signature)
        all_test_scenarios.append(cleaned)


def build_context_prompt(current_state, depth, elements, current_path, preferred_xpaths):
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

    app_hints = config.get("app_hints", [])
    if app_hints:
        prompt += "\nAPP_HINTS:\n"
        for hint in app_hints:
            prompt += f"- {hint}\n"
    prompt += """
MENTOR_GUIDANCE:
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
- If no meaningful untried business action remains, return BACKTRACK.
"""


    return prompt


def recursive_explore(current_state, current_path, depth, max_depth=12):
    if depth > max_depth:
        add_scenario_if_new(current_path.copy())
        return

    doms_folder = os.path.join(BASE_CRAWL_DIR, "doms")
    current_dom_path = os.path.join(doms_folder, current_state)

    if not os.path.exists(current_dom_path):
        print(f"⚠️ DOM bulunamadı: {current_dom_path}")
        add_scenario_if_new(current_path.copy())
        return

    if current_state not in memory_visited_ids:
        memory_visited_ids[current_state] = []

    local_iterations = 0
    max_local_iterations = 10

    while local_iterations < max_local_iterations:
        local_iterations += 1

        print(f"\n[{depth}. DERİNLİK] Mentor: '{current_state}' inceleniyor...")

        elements = extract_actionable_skeleton(current_dom_path)

        if not elements:
            print("⚠️ Bu sayfada etkileşebilir element bulunamadı.")
            add_scenario_if_new(current_path.copy())
            break

        state_id = current_state.replace(".html", "")
        preferred_xpaths = {
            normalize_xpath(xp)
            for xp in STATE_CANDIDATE_XPATHS.get(state_id, set())
        }

        prompt = build_context_prompt(
            current_state=current_state,
            depth=depth,
            elements=elements,
            current_path=current_path,
            preferred_xpaths=preferred_xpaths
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
            print("⚠️ LLM çözülemedi; sadece acil fallback kullanılıyor.")
            decision = build_fallback_decision(
                elements,
                visited_ids=memory_visited_ids[current_state],
                preferred_xpaths=preferred_xpaths
            )

        actions_list = decision.get("actions", [])

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

        for act in actions_list:
            sid = act.get("selected_id")
            el = next((e for e in elements if e["llm_id"] == sid), None)

            if not el:
                print(f"⚠️ Element bulunamadı: {sid}")
                continue

            action = normalize_text(act.get("action", "click"))
            stable_xpath = build_stable_xpath(el, act.get("robust_xpath", ""))

            if not stable_xpath:
                print(f"⚠️ Geçersiz locator atlandı: {el.get('text', '')}")
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

            input_value = act.get("input_value", "") or ""

            step = {
                "selected_id": sid,
                "element": el.get("text", "") or el.get("placeholder", "") or el.get("label_text", ""),
                "tag": el.get("tag", ""),
                "type_attr": el.get("type_attr", ""),
                "xpath": stable_xpath,
                "action": action,
                "input_value": input_value,
                "options": el.get("options", []),
                "scenario_reason": scenario_reason,
            }
            # Happy-path senaryoda required-like alanlar dolmadan Sign Up/Register/Save/Create/Submit tıklanmasın.
            if is_commit_like_click(el, action) and not is_negative_scenario(scenario_reason):
                required_ids = set(get_required_like_field_ids(elements))
                filled_ids = filled_field_ids_in_path(temp_path)

                missing_ids = sorted(list(required_ids - filled_ids))

                if missing_ids:
                    print(
                        f"⏭️ Commit/Register/Save ertelendi; happy-path için eksik alanlar var: {missing_ids}"
                    )
                    continue
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

            last_xpath = stable_xpath
            last_text = step["element"]
            last_action = action
            last_feature = feature

            print(
                f"   ✅ LLM Adım: {action.upper()} -> {step['element']} | "
                f"{stable_xpath} | value={repr(input_value)} | feature={feature}"
            )

        if not applied_any_action:
            print("⚠️ Uygulanabilir yeni LLM aksiyonu kalmadı.")
            add_scenario_if_new(temp_path.copy())
            break

        if should_continue_same_state(last_action):
            print("↪️ Input/Select sonrası aynı state üzerinde LLM ile devam ediliyor.")
            current_path[:] = temp_path
            continue

        next_st = find_next_state(current_state, last_xpath, last_text)

        if next_st:
            recursive_explore(next_st, temp_path, depth + 1, max_depth)
        else:
            if is_same_state_exploration_click(last_text):
                print("↪️ Yeni state yok ama yüksek değerli UI branch olabilir; aynı state üzerinde LLM ile devam ediliyor.")
                current_path[:] = temp_path
                continue

            print("🏁 Test Tamamlandı (Yaprak).")
            add_scenario_if_new(temp_path.copy())

    if local_iterations >= max_local_iterations:
        print(f"⚠️ State iteration limiti doldu: {current_state}")
        add_scenario_if_new(current_path.copy())


if __name__ == "__main__":
    print("\n" + "🚀" * 10 + " LLM-CENTRIC RLM TEST MOTORU BAŞLATILDI " + "🚀" * 10)

    load_transition_map(BASE_CRAWL_DIR)
    recursive_explore("index.html", [], 1)

    print("\n" + "=" * 50)
    print(f"🏆 TOPLAM {len(all_test_scenarios)} LLM-TABANLI SENARYO ÜRETİLDİ!")
    print("=" * 50)

    generate_dante_suites(
        all_test_scenarios,
        app_name=APP_NAME,
        login_config=config.get("login"),
        wait_time=config.get("wait_time", 1000),
        auto_deploy=True,
        repo_root=REPO_ROOT,
    )