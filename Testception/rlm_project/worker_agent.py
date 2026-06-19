import os
import re
from rlm import RLM


PLACEHOLDER_RESPONSES = {
    "final_output",
    "final_response",
    "final_json_output",
    "formatted_json_response",
    "final_json_response",
    "final_answer",
    "final_action_response",
    "json_final_output",
    "json_response_participant",
    "backtrack_response",
    "json_output",
    "json_response",
    "final_json_string",
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


def fallback_input_value(el):
    """
    Sadece LLM parse edilemezse/boş input döndürürse kullanılan son çare.
    Normal durumda input değerini LLM üretmelidir.
    """
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
            if normalize_text(opt)
               and "select" not in normalize_text(opt)
               and "choose" not in normalize_text(opt)
        ]

        # Coverage için default/ilk seçeneği değil, mümkünse ikinci/alternatif değeri seç.
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


def is_obviously_low_value_element(el):
    text = normalize_text(el.get("text", ""))
    title = normalize_text(el.get("title_attr", ""))
    aria = normalize_text(el.get("aria_label", ""))
    href_like = " ".join([text, title, aria])

    low_value_keywords = [
        "facebook",
        "twitter",
        "github",
        "source code",
        "language",
        "about",
        "terms",
        "privacy",
        "home page",
    ]

    return any(k in href_like for k in low_value_keywords)


def score_element_for_fallback(el, visited_ids=None, preferred_xpaths=None):
    visited_ids = set(visited_ids or [])
    preferred_xpaths = set(preferred_xpaths or [])

    if el.get("llm_id") in visited_ids:
        return -9999

    if is_obviously_low_value_element(el):
        return -200

    tag = normalize_text(el.get("tag", ""))
    text = normalize_text(el.get("text", ""))
    placeholder = normalize_text(el.get("placeholder", ""))
    label = normalize_text(el.get("label_text", ""))
    title = normalize_text(el.get("title_attr", ""))
    aria = normalize_text(el.get("aria_label", ""))
    role = normalize_text(el.get("role", ""))
    klass = normalize_text(el.get("class_attr", ""))
    xp = normalize_xpath(el.get("xpath", ""))

    combined = " ".join([text, placeholder, label, title, aria, role, klass])

    score = 0

    if xp in preferred_xpaths:
        score += 80

    if tag in {"input", "textarea", "select"}:
        score += 70

    if tag in {"button", "a"}:
        score += 50

    if tag in {"div", "span"}:
        score += 10

    high_value_keywords = [
        "create", "save", "submit", "add", "new",
        "edit", "delete", "remove", "update",
        "transaction", "payment", "amount", "wallet", "goal", "filter", "import",
        "owner", "pet", "visit", "veterinarian", "specialty", "type",
        "board", "card", "list",
        "session", "retro", "note",
        "user", "participant", "share", "details",
        "login", "register", "search",
        "checkout", "cart", "upload", "download", "+"
    ]

    if any(k in combined for k in high_value_keywords):
        score += 60

    return score


def build_backtrack():
    return {
        "scenario_reason": "No meaningful untried business interaction remains on this state, so I should backtrack.",
        "actions": [
            {
                "selected_id": "NONE",
                "action": "BACKTRACK",
                "input_value": "",
                "robust_xpath": ""
            }
        ]
    }


def build_fallback_decision(elements, visited_ids=None, preferred_xpaths=None):
    scored = []
    for el in elements:
        scored.append((score_element_for_fallback(el, visited_ids, preferred_xpaths), el))

    scored.sort(key=lambda x: x[0], reverse=True)

    if not scored or scored[0][0] < 0:
        return build_backtrack()

    best = scored[0][1]
    tag = normalize_text(best.get("tag", ""))

    type_attr = normalize_text(best.get("type_attr", ""))

    if tag == "input" and type_attr in {"submit", "button", "reset"}:
        action = "click"
        input_value = ""

    elif tag in {"input", "textarea"}:
        action = "input"
        input_value = fallback_input_value(best)

    elif tag == "select":
        action = "select"
        input_value = fallback_input_value(best)

    else:
        action = "click"
        input_value = ""

    return {
        "scenario_reason": f"Fallback: choosing the highest-value untried interactive element: {best.get('text') or best.get('placeholder') or best.get('xpath')}",
        "actions": [
            {
                "selected_id": best["llm_id"],
                "action": action,
                "input_value": input_value,
                "robust_xpath": best["xpath"]
            }
        ]
    }


def validate_llm_decision(decision, elements):
    if not isinstance(decision, dict):
        return None

    valid_ids = {el["llm_id"] for el in elements}
    actions = decision.get("actions", [])

    if not isinstance(actions, list) or not actions:
        return None

    cleaned_actions = []

    for act in actions:
        if not isinstance(act, dict):
            continue

        sid = act.get("selected_id")
        if isinstance(sid, str):
            sid = sid.strip().replace("[", "").replace("]", "")

        action = str(act.get("action", "click")).strip().lower()
        robust_xpath = act.get("robust_xpath", "")
        input_value = act.get("input_value", "")

        if sid == "NONE" and action == "backtrack":
            cleaned_actions.append({
                "selected_id": "NONE",
                "action": "BACKTRACK",
                "input_value": "",
                "robust_xpath": ""
            })
            continue

        if sid not in valid_ids:
            continue

        matched = next((e for e in elements if e["llm_id"] == sid), None)
        if not matched:
            continue

        if isinstance(robust_xpath, str):
            robust_xpath = robust_xpath.replace("\\'", "'").strip()

        tag = normalize_text(matched.get("tag", ""))

        type_attr = normalize_text(matched.get("type_attr", ""))

        # input type=submit/button/reset gerçek veri girişi değildir, tıklanmalıdır.
        if tag == "input" and type_attr in {"submit", "button", "reset"}:
            action = "click"
            input_value = ""

        elif tag in {"input", "textarea"}:
            action = "input"
            if input_value is None or str(input_value).strip() == "":
                input_value = fallback_input_value(matched)

        elif tag == "select":
            action = "select"
            if input_value is None or str(input_value).strip() == "":
                input_value = fallback_input_value(matched)
        else:
            if action not in {"click", "hover", "check", "uncheck"}:
                action = "click"
            if action in {"hover"}:
                action = "click"
            input_value = ""

        cleaned_actions.append({
            "selected_id": sid,
            "action": action,
            "input_value": str(input_value),
            "robust_xpath": robust_xpath or matched.get("xpath", "")
        })

    if not cleaned_actions:
        return None

    return {
        "scenario_reason": decision.get("scenario_reason", "LLM selected QA action"),
        "actions": cleaned_actions
    }


def ask_worker_llm(prompt_text, elements=None, visited_ids=None, preferred_xpaths=None, max_actions=15):
    print("🧠 Worker LLM sayfayı QA mühendisi gibi analiz ediyor...")

    elements = elements or []
    visited_ids = visited_ids or []
    preferred_xpaths = preferred_xpaths or set()
    max_actions = max(1, min(int(max_actions), 6))

    agent = RLM(
        backend="litellm",
        backend_kwargs={
            "model_name": "openrouter/openai/gpt-4o-mini",
            "api_key": os.environ.get("OPENROUTER_API_KEY", ""),
            #"max_tokens": 2000,
            "temperature": 0.2,
        },
        environment="local",
        environment_kwargs={},
        max_depth=1,
        verbose=False,
    )


    qa_master_prompt = f"""
ROLE: Senior QA Automation Engineer.

TASK:
Choose the next meaningful web test action sequence from the DOM skeleton.
Return ONLY valid JSON. No markdown. No explanation outside JSON.
Your response must start with {{ and end with }}.

PRIORITIES:
1. Prefer business flows over static links.
2. Prefer CRUD/business actions: add, create, edit, delete, save, submit, details, transaction, wallet, card, owner, pet, visit.
3. Static/outbound links are lower priority, but test one later if no business action remains.
4. Avoid repeating already tried actions unless needed for a new valid flow.
5. Choose at most {max_actions} actions.

FORM RULES:
- Negative validation test is allowed: incomplete submit can be tested if scenario_reason says Negative/Validation/Invalid.
- Happy-path form sequence must be:
  fill required-looking inputs -> select required-looking selects -> click final Save/Create/Submit/Register/Sign Up/Add.
- In happy-path, submit/commit must be the LAST action.
- Do not click Delete before creating or reaching a valid entity unless scenario_reason says destructive/negative.
- Add/New/+ often opens a form/modal. Treat it as click, not final submit, unless it is clearly input/button type=submit.

SELECT RULES:
- For select/dropdown, choose a non-default valid option.
- input_value must exactly match one visible option.

ACTION MAP:
- input/textarea -> action "input", input_value required.
- select -> action "select", input_value required.
- button/a/link/tab/modal trigger -> action "click", input_value "".
- selected_id must be one of the provided el_x IDs.
- If no meaningful untried action remains, return BACKTRACK.

JSON FORMAT:
{{
  "scenario_reason": "Short reason",
  "actions": [
    {{
      "selected_id": "el_1",
      "robust_xpath": "//INPUT[@name='email']",
      "action": "input",
      "input_value": "qa.user@example.com"
    }}
  ]
}}

BACKTRACK FORMAT:
{{
  "scenario_reason": "No meaningful untried business action remains.",
  "actions": [
    {{
      "selected_id": "NONE",
      "robust_xpath": "",
      "action": "BACKTRACK",
      "input_value": ""
    }}
  ]
}}
"""


    final_prompt = prompt_text + "\n\n" + qa_master_prompt

    try:
        result = agent.completion(final_prompt)
        raw = result.response

        if isinstance(raw, str) and normalize_text(raw) in PLACEHOLDER_RESPONSES:
            print("⚠️ LLM placeholder cevap döndürdü, fallback kullanılacak.")
            return build_fallback_decision(elements, visited_ids, preferred_xpaths)

        return raw

    except Exception as e:
        print(f"⚠️ LLM çağrısı hata verdi: {e}. Fallback kullanılacak.")
        return build_fallback_decision(elements, visited_ids, preferred_xpaths)
