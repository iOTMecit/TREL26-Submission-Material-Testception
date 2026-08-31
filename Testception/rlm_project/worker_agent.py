import os
import re
import instructor
from pydantic import BaseModel, Field
from litellm import completion

LLM_MODEL = os.getenv(
    "TESTCEPTION_LLM_MODEL",
    "",
).strip()

OPENROUTER_API_KEY = os.getenv(
    "OPENROUTER_API_KEY",
    "",
).strip()


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


class TestAction(BaseModel):
    selected_id: str = Field(description="Seçilen elementin llm_id değeri (örn: el_5). İşlem kalmadıysa 'NONE'")
    robust_xpath: str = Field(description="Seçilen elementin xpath değeri. Yoksa boş bırak.")
    action: str = Field(description="Sadece 'click', 'input', veya 'select' olabilir. İşlem kalmadıysa 'BACKTRACK'")
    input_value: str = Field(description="Eğer action 'input' veya 'select' ise girilecek değer. Yoksa boş bırak.")

class TestDecision(BaseModel):
    scenario_reason: str = Field(description="QA Perspektifiyle bu senaryonun amacı nedir?")
    actions: list[TestAction] = Field(description="Uygulanacak eylemler listesi.")

# LiteLLM'i Instructor ile sarıyoruz
client = instructor.from_litellm(completion)

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

    if tag == "input" and type_attr in {
        "submit", "button", "reset", "checkbox", "radio"
    }:
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


def match_element_by_locator(elements, selected_id, robust_xpath):
    if selected_id:
        matched = next(
            (
                element
                for element in elements
                if element.get("llm_id") == selected_id
            ),
            None,
        )
        if matched:
            return matched

    target_xpath = normalize_xpath(robust_xpath)
    if not target_xpath:
        return None

    for element in elements:
        if target_xpath in {
            normalize_xpath(element.get("xpath", "")),
            normalize_xpath(element.get("absolute_xpath", "")),
        }:
            return element

    return None


def validate_llm_decision(decision, elements):
    if not isinstance(decision, dict):
        return None

    actions = decision.get("actions", [])
    if not isinstance(actions, list) or not actions:
        return None

    cleaned_actions = []
    backtrack_requested = False
    seen_locators = set()

    for act in actions:
        if not isinstance(act, dict):
            continue

        sid = act.get("selected_id")
        if isinstance(sid, str):
            sid = sid.strip().replace("[", "").replace("]", "")

        action = str(act.get("action", "click")).strip().lower()
        robust_xpath = act.get("robust_xpath", "")
        input_value = act.get("input_value", "")

        if sid == "NONE" or action == "backtrack":
            backtrack_requested = True
            continue

        if isinstance(robust_xpath, str):
            robust_xpath = robust_xpath.replace("\\'", "'").strip()

        matched = match_element_by_locator(
            elements,
            sid,
            robust_xpath,
        )
        if not matched:
            continue

        sid = matched.get("llm_id")
        tag = normalize_text(matched.get("tag", ""))
        type_attr = normalize_text(matched.get("type_attr", ""))

        if tag == "input" and type_attr in {
            "submit",
            "button",
            "reset",
            "checkbox",
            "radio",
        }:
            action = "click"
            input_value = ""

        elif tag in {"input", "textarea"}:
            action = "input"
            if input_value is None or str(input_value).strip() == "":
                input_value = fallback_input_value(matched)

        elif tag in {"select", "mat-select"}:
            action = "select"
            if input_value is None or str(input_value).strip() == "":
                input_value = fallback_input_value(matched)

        else:
            action = "click"
            input_value = ""

        locator_key = (
            action,
            normalize_xpath(
                matched.get("xpath", "")
                or matched.get("absolute_xpath", "")
                or robust_xpath
            ),
        )

        # Multiple different values for the same field in one LLM response
        # are not a valid sequential form plan.
        if locator_key in seen_locators:
            continue
        seen_locators.add(locator_key)

        cleaned_actions.append({
            "selected_id": sid,
            "action": action,
            "input_value": str(input_value),
            "robust_xpath": (
                matched.get("xpath", "")
                or matched.get(
                    "absolute_xpath",
                    "",)
            ),
        })

    if cleaned_actions:
        return {
            "scenario_reason": decision.get(
                "scenario_reason",
                "LLM selected QA action",
            ),
            "actions": cleaned_actions,
        }

    if backtrack_requested:
        return build_backtrack()

    return None


def ask_worker_llm(prompt_text, elements=None, visited_ids=None, preferred_xpaths=None, max_actions=15):
    print("🧠 Worker LLM (Instructor & Pydantic) sayfayı analiz ediyor...")

    elements = elements or []
    visited_ids = visited_ids or []
    preferred_xpaths = preferred_xpaths or set()
    # The orchestrator already asks for 15 actions. The old hard clamp of 6
    # silently prevented the Worker from using that budget on larger forms.
    max_actions = max(1, min(int(max_actions), 20))

    worker_max_tokens = max(
        1000,
        min(
            int(os.getenv("TESTCEPTION_WORKER_MAX_TOKENS", "2200")),
            6000,
        ),
    )

    # Dev JSON kurallarını sildik, prompt %60 küçüldü.
    qa_master_prompt = f"""
    ROLE:
    Senior QA Automation Engineer.

    TASK:
    Choose the next meaningful web test action sequence
    from the DOM skeleton.

    Choose at most {max_actions} actions.

    The user prompt contains an exploration_mode value:

    - GRAPH_FIRST
    - DOM_FALLBACK

    PRIORITIES & RULES:

    1. Prefer business flows such as CRUD, Add, Create,
       Save, Edit, Update, Delete, Visit, Transaction,
       Details, Search and Filter over static links.

    2. Negative validation testing is allowed only when
       the scenario explicitly requests a negative test.

    3. Happy-path form sequence:
       - fill required inputs;
       - select valid options;
       - click the final Save/Add/Update/Submit control.
       The submit click must be the LAST action.

    4. Do not repeat elements marked [ALREADY_TRIED]
       unless they are required to reach a new business
       action.

    5. Action mapping:
       - input and textarea -> input
       - select and mat-select -> select
       - button and a -> click

    6. When exploration_mode is GRAPH_FIRST:

       - Prefer elements marked [RECORDED_UNTRIED].
       - result.json transitions have priority over
         DOM-only discovery actions.
       - Visible form inputs and selects may be used
         before the recorded submit transition.
       - Do not choose a
         [DOM_DISCOVERY_CANDIDATE] click while an
         untried recorded transition remains.
       - Choose exactly one state-changing click.
       - Do not include actions belonging to the
         destination page in the same response.

    7. When exploration_mode is DOM_FALLBACK:

       - All known result.json transitions for the
         current state have already been explored.
       - Choose a meaningful untried element marked
         [DOM_DISCOVERY_CANDIDATE].
       - DOM_FALLBACK is iterative: the Mentor may call you again on the same
         recorded state after the selected DOM-only click is saved as a leaf.
         Therefore do NOT BACKTRACK while another meaningful untried business
         candidate is still present.
       - Prefer distinct business controls such as Transactions, Add
         Transaction, Share, Edit, Delete, Save, tabs, dropdowns and modal
         actions over repeated New Event/Add Participant actions.
       - Prefer business controls over Home, logo,
         footer, social, About, Source or external links.
       - A DOM-only form must still be completed:
         fill its visible fields and click its submit
         control last.
       - Choose exactly one state-changing click.

    8. Elements marked [RECORDED_EXPLORED] have already
       been tested. Do not select them unless they are
       a necessary prerequisite for reaching a new
       DOM discovery action.

    9. Use each form field at most once in one response.

    10. When a visible Save, Add, Create, Update or Submit
        control belongs to the current form, include it
        after the form fields. Do not return only input
        actions for a positive form scenario.

    11. Do not return NONE or BACKTRACK together with
        normal actions.

    12. Return BACKTRACK with selected_id NONE only when:

        - no [RECORDED_UNTRIED] transition remains;
        - no meaningful [DOM_DISCOVERY_CANDIDATE]
          remains;
        - no incomplete business form remains.
    """

    if not LLM_MODEL:
        raise RuntimeError(
            "TESTCEPTION_LLM_MODEL tanımlı değil. "
            "Modeli terminal oturumunda export edin."
        )

    if not OPENROUTER_API_KEY:
        raise RuntimeError(
            "OPENROUTER_API_KEY tanımlı değil. "
            "API key'i terminal oturumunda export edin."
        )

    try:
        # Instructor response_model parametresiyle JSON'u garantiye alıyoruz
        decision = client.chat.completions.create(
            model=LLM_MODEL,
            response_model=TestDecision,
            messages=[
                {"role": "system", "content": qa_master_prompt},
                {"role": "user", "content": prompt_text}
            ],
            api_key=OPENROUTER_API_KEY,
            temperature=0.2,
            max_tokens=worker_max_tokens
        )

        # Gelen Pydantic objesini doğrudan dict olarak orkestratöre geri yolluyoruz
        return decision.model_dump()

    except Exception as e:
        print(f"⚠️ LLM çağrısı hata verdi: {e}. Fallback kullanılacak.")
        return build_backtrack()
