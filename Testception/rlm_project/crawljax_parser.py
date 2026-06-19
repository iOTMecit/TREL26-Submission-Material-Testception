import os
from bs4 import BeautifulSoup


def get_absolute_xpath(element):
    components = []
    current = element

    while current is not None and current.name not in ["document", "[document]"]:
        index = 1
        for sibling in current.previous_siblings:
            if getattr(sibling, "name", None) == current.name:
                index += 1

        components.append(f"{current.name.upper()}[{index}]")
        current = current.parent

    components.reverse()
    return "/" + "/".join(components) if components else ""


def get_label_text(element):
    el_id = element.get("id")
    if not el_id:
        return ""

    root = element
    while getattr(root, "parent", None) is not None:
        root = root.parent

    try:
        label = root.find("label", attrs={"for": el_id})
        if label:
            return label.get_text(" ", strip=True)
    except Exception:
        pass

    return ""


def get_crawljax_xpath(element):
    if not element or not getattr(element, "name", None):
        return ""

    tag = element.name.upper()

    el_id = element.get("id")
    if el_id:
        return f"//{tag}[@id = '{el_id}']"

    el_name = element.get("name")
    if el_name:
        return f"//{tag}[@name = '{el_name}']"

    title = element.get("title")
    if title:
        return f"//{tag}[@title = '{title}']"

    placeholder = element.get("placeholder")
    if placeholder:
        return f"//{tag}[@placeholder = '{placeholder}']"

    aria_label = element.get("aria-label")
    if aria_label:
        return f"//{tag}[@aria-label = '{aria_label}']"

    text = element.get_text(" ", strip=True)
    if text and len(text) <= 70 and "'" not in text:
        return f"//{tag}[contains(normalize-space(.), '{text}')]"

    return get_absolute_xpath(element)


def is_hidden_or_useless(el):
    if el.name == "input" and el.get("type") == "hidden":
        return True

    style = el.get("style", "").replace(" ", "").lower()
    if "display:none" in style:
        return True

    if el.get("hidden") is not None:
        return True

    if el.get("aria-hidden", "").lower() == "true":
        return True

    return False


def is_clickable_container(el):
    """
    DIV/SPAN gibi container elementleri sadece gerçekten etkileşimli görünüyorsa actionable al.
    Bu parser kararı değildir; sadece çok büyük wrapper'ları LLM listesine sokmamak içindir.
    """
    if el.name not in {"div", "span"}:
        return True

    text = el.get_text(" ", strip=True).lower()
    role = (el.get("role") or "").lower()
    klass = " ".join(el.get("class", [])).lower()
    onclick = el.get("onclick")

    if role in {"button", "tab", "menuitem", "option", "checkbox", "radio"}:
        return True

    if onclick:
        return True

    interactive_classes = [
        "btn",
        "button",
        "dropdown",
        "modal",
        "tab",
        "floating",
        "fab",
        "select",
        "menu",
        "item",
    ]

    if any(k in klass for k in interactive_classes):
        return True

    if text.strip() == "+":
        return True

    # Büyük wrapper/container ele.
    if len(text) > 100:
        return False

    return False


def extract_actionable_skeleton(html_filepath):
    if not os.path.exists(html_filepath):
        return []

    with open(html_filepath, "r", encoding="utf-8", errors="ignore") as f:
        soup = BeautifulSoup(f.read(), "html.parser")

    actionable_elements = []
    target_tags = ["button", "a", "input", "select", "textarea", "div", "span"]
    elements = soup.find_all(target_tags)

    visible_index = 0

    for el in elements:
        if is_hidden_or_useless(el):
            continue

        if not is_clickable_container(el):
            continue

        text = el.get_text(" ", strip=True)
        if not text:
            text = (
                    el.get("value", "")
                    or el.get("placeholder", "")
                    or el.get("title", "")
                    or el.get("aria-label", "")
                    or get_label_text(el)
            )

        # ✅ Icon / boş text butonları için semantik isim üret.
        if not text:
            attrs_blob = " ".join([
                el.get("id", "") or "",
                el.get("name", "") or "",
                el.get("role", "") or "",
                " ".join(el.get("class", [])) if el.get("class") else "",
                el.get("onclick", "") or "",
            ]).lower()

            if any(k in attrs_blob for k in ["add", "new", "create"]):
                text = "Add"

            elif any(k in attrs_blob for k in ["plus", "glyphicon-plus", "fa-plus"]):
                text = "+ Add"

            elif any(k in attrs_blob for k in ["edit", "pencil"]):
                text = "Edit"

            elif any(k in attrs_blob for k in ["delete", "remove", "trash"]):
                text = "Delete"

            elif any(k in attrs_blob for k in ["close", "times"]):
                text = "Close"

        options = []
        if el.name == "select":
            options = [
                opt.get_text(" ", strip=True)
                for opt in el.find_all("option")
                if opt.get_text(" ", strip=True)
            ]

        actionable_elements.append({
            "llm_id": f"el_{visible_index}",
            "tag": el.name,
            "type_attr": el.get("type", ""),
            "text": text[:180],
            "id_attr": el.get("id", ""),
            "name_attr": el.get("name", ""),
            "title_attr": el.get("title", ""),
            "placeholder": el.get("placeholder", ""),
            "aria_label": el.get("aria-label", ""),
            "label_text": get_label_text(el),
            "role": el.get("role", ""),
            "class_attr": " ".join(el.get("class", [])),
            "options": options[:20],
            "xpath": get_crawljax_xpath(el),
            "absolute_xpath": get_absolute_xpath(el),
        })

        visible_index += 1
    seen = {}
    for item in actionable_elements:
        xp = item.get("xpath", "")
        seen.setdefault(xp, []).append(item)

    for xp, items in seen.items():
        if len(items) > 1:
            for item in items:
                # item içine absolute_xpath daha önce koymadıysak aşağıdaki küçük ekleme gerekir
                if item.get("absolute_xpath"):
                    item["xpath"] = item["absolute_xpath"]
    return actionable_elements


def generate_worker_prompt(state_name, elements, visited_ids, preferred_xpaths=None):
    preferred_xpaths = preferred_xpaths or set()

    prompt = f"CURRENT_STATE_FILE: {state_name}\n\n"
    prompt += "ACTIONABLE_DOM_SKELETON:\n"

    for el in elements:
        status = "[ALREADY_TRIED] " if el["llm_id"] in visited_ids else ""
        preferred = "[CRAWL_TRANSITION_CANDIDATE] " if el["xpath"].replace(" ", "").lower() in preferred_xpaths else ""

        line = (
            f"- ID: {el['llm_id']} {status}{preferred}"
            f"| tag={el['tag']} "
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
            f"| xpath={el['xpath']}"
        )

        if el["tag"] == "select" and el.get("options"):
            line += f" | options={el['options']}"

        prompt += line + "\n"

    return prompt