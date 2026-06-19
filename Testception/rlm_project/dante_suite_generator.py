import shutil
import re


def is_invalid_xpath(xpath):
    if not xpath:
        return True

    xp = xpath.strip()

    if not xp:
        return True

    low = xp.lower()

    if "@id = 'el_" in low or '@id="el_' in low:
        return True

    if re.search(r"@id\s*=\s*['\"]ember\d+['\"]", low):
        return True

    return False


def escape_java_string(value):
    return (value or "").replace("\\", "\\\\").replace('"', '\\"')


def sanitize_scenario(scenario):
    cleaned = []
    seen = set()

    for step in scenario:
        xpath = (step.get("xpath") or "").strip()

        if is_invalid_xpath(xpath):
            continue

        action = (step.get("action") or "click").lower()
        input_value = step.get("input_value", "") or ""
        feature_bucket = step.get("feature_bucket", "") or ""
        feature_signature = step.get("feature_signature", "") or ""
        state_type = step.get("state_type", "") or ""
        scenario_reason = step.get("scenario_reason", "") or ""
        element = step.get("element", "") or ""

        signature = (
            action,
            xpath,
            input_value,
            feature_bucket,
            feature_signature,
            state_type,
            element,
        )

        if signature in seen:
            continue

        seen.add(signature)

        cleaned.append({
            "element": element,
            "xpath": xpath,
            "action": action,
            "input_value": input_value,
            "options": step.get("options", []),
            "tag": step.get("tag", ""),
            "type_attr": step.get("type_attr", ""),
            "feature_bucket": feature_bucket,
            "feature_signature": feature_signature,
            "state_type": state_type,
            "scenario_reason": scenario_reason,
        })

    return cleaned
def normalize_for_prefix(value):
    if value is None:
        return ""
    value = str(value).strip().lower()
    value = re.sub(r"\s+", " ", value)
    return value


def step_prefix_key(step):
    """
    Prefix karşılaştırması için step'i normalize eder.
    Burada amaç sadece birebir aynı aksiyon zincirini yakalamak.
    """
    action = normalize_for_prefix(step.get("action", ""))
    xpath = normalize_for_prefix(step.get("xpath", ""))
    input_value = normalize_for_prefix(step.get("input_value", ""))
    feature_signature = normalize_for_prefix(step.get("feature_signature", ""))

    # Click için input_value anlamsız.
    if action == "click":
        input_value = ""

    return (action, xpath, input_value, feature_signature)


def scenario_prefix_key(scenario):
    return tuple(step_prefix_key(step) for step in scenario)


def is_exact_prefix(shorter, longer):
    """
    shorter, longer senaryosunun baştan birebir prefix'i mi?
    Sadece bu durumda True döner.
    """
    if len(shorter) >= len(longer):
        return False

    shorter_key = scenario_prefix_key(shorter)
    longer_key = scenario_prefix_key(longer)

    return longer_key[:len(shorter_key)] == shorter_key


def remove_exact_prefix_scenarios(valid_scenarios):
    """
    Sadece başka bir senaryonun başında birebir yer alan kısa senaryoları çıkarır.
    Kümeleme, skor, benzerlik, agresif eleme yapmaz.
    """
    keep = [True] * len(valid_scenarios)

    for i, scenario_i in enumerate(valid_scenarios):
        for j, scenario_j in enumerate(valid_scenarios):
            if i == j:
                continue

            if is_exact_prefix(scenario_i, scenario_j):
                keep[i] = False
                break

    reduced = [
        scenario
        for scenario, should_keep in zip(valid_scenarios, keep)
        if should_keep
    ]

    print(
        f"🧹 Exact prefix reduction: raw={len(valid_scenarios)} "
        f"removed={len(valid_scenarios) - len(reduced)} "
        f"final={len(reduced)}"
    )

    return reduced

def is_commit_step(step):
    feature = (step.get("feature_bucket") or "").lower()
    signature = (step.get("feature_signature") or "").lower()
    xpath = (step.get("xpath") or "").lower()
    element = (step.get("element") or "").lower()
    action = (step.get("action") or "").lower()
    tag = (step.get("tag") or "").lower()
    type_attr = (step.get("type_attr") or "").lower()

    if action != "click":
        return False
    # Header/navbar/container commit değildir.
    if tag in {"div", "span"} and any(k in element for k in [
        "home", "wallets", "goals", "contact", "api", "settings", "log out",
        "owners", "veterinarians", "pet types", "specialties",
        "boards", "sign out", "source code", "github",
    ]):
        return False

    # Modal/dialog container'ın kendisi commit değildir.
    if tag in {"div", "span"} and any(k in element for k in [
        "registration", "sign in", "username", "password", "invalid username",
        "modal", "dialog"
    ]):
        return False
    # Register / Sign In gibi form açan linkler commit değildir.
    if signature.startswith("add_or_open:"):
        return False

    if tag == "a" and any(k in element for k in [
        "register",
        "sign in",
        "sign up",
        "login",
        "create new",
        "new event",
    ]):
        return False

    if feature in {
        "create_form_invalid_commit",
        "create_form_valid_commit",
        "create_form_commit",
        "transaction_commit",
        "edit_event_save",
        "quick_add_submit",
    }:
        return True

    if signature.startswith("commit:"):
        return True

    if tag == "input" and type_attr in {"submit", "button"}:
        return True

    combined = xpath + " " + element + " " + signature

    return any(k in combined for k in [
        "save",
        "create",
        "submit",
        "done",
        "register",
        "sign up",
        "sign in",
        "login",
        "type='submit'",
        'type="submit"',
    ])




def get_tests_output_dir(app_name, repo_root="/home/mecit/workspace/TREL26-Submission-Material-Testception"):
    return os.path.join(
        repo_root,
        "dante",
        "applications",
        app_name,
        f"testsuite-{app_name}",
        "src",
        "main",
        "java",
        "tests",
    )


def deploy_generated_suites(file_normal, file_coverage, app_name, repo_root="/home/mecit/workspace/TREL26-Submission-Material-Testception"):
    output_dir = get_tests_output_dir(app_name, repo_root=repo_root)
    os.makedirs(output_dir, exist_ok=True)

    target_normal = os.path.join(output_dir, os.path.basename(file_normal))
    target_coverage = os.path.join(output_dir, os.path.basename(file_coverage))

    shutil.copy2(file_normal, target_normal)
    shutil.copy2(file_coverage, target_coverage)

    print(f"📦 Auto-deployed: {target_normal}")
    print(f"📦 Auto-deployed: {target_coverage}")

    return target_normal, target_coverage

def generate_dante_suites(
    all_scenarios,
    app_name="ecommerce",
    login_config=None,
    wait_time=1000,
    auto_deploy=True,
    repo_root="/home/mecit/workspace/TREL26-Submission-Material-Testception",
):
    print(
        "\n"
        + "☕" * 10
        + f" GENERATING DANTE-COMPATIBLE JAVA TEST CLASSES ({app_name.upper()}) "
        + "☕" * 10
    )

    strategy = "Checked" if app_name in ["phoenix", "splittypie"] else "Fired"

    class_name_normal = f"GeneratedTestSuite{strategy}"
    class_name_coverage = f"GeneratedTestSuite{strategy}Coverage"

    helper_methods = """
\tprivate void highlight(WebElement element) {
\t\ttry {
\t\t\t((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
\t\t\t\t"arguments[0].scrollIntoView({block: 'center', inline: 'center'});" +
\t\t\t\t"arguments[0].style.border='4px solid red';" +
\t\t\t\t"arguments[0].style.backgroundColor='yellow';",
\t\t\t\telement
\t\t\t);
\t\t\tThread.sleep(700);
\t\t} catch (Exception e) {}
\t}

\tprivate boolean isVisibleAndEnabled(WebElement element) {
\t\ttry {
\t\t\treturn element != null && element.isDisplayed() && element.isEnabled();
\t\t} catch (Exception e) {
\t\t\treturn false;
\t\t}
\t}

\tprivate String norm(String value) {
\t\tif (value == null) return "";
\t\treturn value.trim().toLowerCase();
\t}

\tprivate void clickElementHard(WebElement element) throws Exception {
\t\thighlight(element);

\t\ttry {
\t\t\telement.click();
\t\t} catch (Exception e1) {
\t\t\ttry {
\t\t\t\t((org.openqa.selenium.JavascriptExecutor) driver)
\t\t\t\t\t.executeScript("arguments[0].click();", element);
\t\t\t} catch (Exception e2) {
\t\t\t\t((org.openqa.selenium.JavascriptExecutor) driver)
\t\t\t\t\t.executeScript(
\t\t\t\t\t\t"var ev = new MouseEvent('click', {bubbles:true, cancelable:true, view:window}); arguments[0].dispatchEvent(ev);",
\t\t\t\t\t\telement
\t\t\t\t\t);
\t\t\t}
\t\t}

\t\tThread.sleep(1500);
\t}
\tprivate WebElement findVisibleElement(String xpath) throws Exception {
\t\tif (xpath == null || xpath.trim().isEmpty()) {
\t\t\tthrow new RuntimeException("Empty xpath");
\t\t}
\t\tList<WebElement> elements = driver.findElements(By.xpath(xpath));
\t\tif (elements == null || elements.isEmpty()) {
\t\t\tthrow new RuntimeException("Element not found: " + xpath);
\t\t}
\t\t// Modal/pop-up durumlarında genellikle son görünen element aktif olur.
\t\tfor (int i = elements.size() - 1; i >= 0; i--) {
\t\t\tWebElement element = elements.get(i);
\t\t\tif (isVisibleAndEnabled(element)) {
\t\t\t\treturn element;
\t\t\t}
\t\t}

		// Hiç visible yoksa Selenium'un hata vermesi yerine son elementi döndürüp
		// JS click/scroll fallback'e şans verelim.
		return elements.get(elements.size() - 1);
	}
\tprivate void safeClick(String xpath) throws Exception {
\tif (xpath == null || xpath.trim().isEmpty()) return;
\t\tWebElement element = findVisibleElement(xpath);
\t\tclickElementHard(element);
\t}
\tprivate void safeSaveButtonClick(String xpath) throws Exception {
\t\tSystem.out.println("SAVE_HELPER_START: " + xpath);

\t\tString[] directXpaths = new String[] {
\t\t\txpath,

\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'save')]",
\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'create')]",
\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'submit')]",
\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'done')]",
\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'register')]",
\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sign up')]",
\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sign in')]",
\t\t\t"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'login')]",
\t\t\t"//button[@type='submit']",
\t\t\t"//input[@type='submit']",
\t\t\t"(//form//button)[last()]",
\t\t};

\t\tException last = null;

\t\tfor (String candidate : directXpaths) {
\t\t\tif (candidate == null || candidate.trim().isEmpty()) continue;

\t\t\ttry {
\t\t\t\tList<WebElement> found = driver.findElements(By.xpath(candidate));
\t\t\t\tSystem.out.println("SAVE_HELPER_XPATH: " + candidate + " count=" + found.size());

\t\t\t\tfor (WebElement element : found) {
\t\t\t\t\tif (!isVisibleAndEnabled(element)) continue;

\t\t\t\t\tString txt = norm(element.getText());
\t\t\t\t\tString val = norm(element.getAttribute("value"));
\t\t\t\t\tString aria = norm(element.getAttribute("aria-label"));
\t\t\t\t\tString type = norm(element.getAttribute("type"));
\t\t\t\t\tString cls = norm(element.getAttribute("class"));

\t\t\t\t\tString combined = txt + " " + val + " " + aria + " " + type + " " + cls;
\t\t\t\t\tSystem.out.println("SAVE_HELPER_CANDIDATE: " + combined);

\t\t\t\t\tif (
\t\t\t\t\t\tcombined.contains("save") ||
\t\t\t\t\t\tcombined.contains("create") ||
\t\t\t\t\t\tcombined.contains("submit") ||
\t\t\t\t\t\tcombined.contains("done") ||
\t\t\t\t\t\tcombined.contains("register") ||
\t\t\t\t\t\tcombined.contains("sign up") ||
\t\t\t\t\t\tcombined.contains("sign in") ||
\t\t\t\t\t\tcombined.contains("login") ||
\t\t\t\t\t\t"submit".equals(type)
\t\t\t\t\t) {
\t\t\t\t\t\tclickElementHard(element);
\t\t\t\t\t\tSystem.out.println("SAVE_HELPER_CLICK_DONE");
\t\t\t\t\t\treturn;
\t\t\t\t\t}
\t\t\t\t}
\t\t\t} catch (Exception e) {
\t\t\t\tlast = e;
\t\t\t}
\t\t}

\t\ttry {
\t\t\tList<WebElement> all = driver.findElements(By.xpath("//button | //a | //input[@type='submit'] | //input[@type='button']"));
\t\t\tSystem.out.println("SAVE_HELPER_SCAN_COUNT=" + all.size());

\t\t\tfor (WebElement element : all) {
\t\t\t\tif (!isVisibleAndEnabled(element)) continue;

\t\t\t\tString txt = norm(element.getText());
\t\t\t\tString val = norm(element.getAttribute("value"));
\t\t\t\tString aria = norm(element.getAttribute("aria-label"));
\t\t\t\tString cls = norm(element.getAttribute("class"));
\t\t\t\tString type = norm(element.getAttribute("type"));

\t\t\t\tString combined = txt + " " + val + " " + aria + " " + cls + " " + type;
\t\t\t\tSystem.out.println("SAVE_HELPER_SCAN_ELEMENT: " + combined);

\t\t\t\tif (
\t\t\t\t\tcombined.contains("save") ||
\t\t\t\t\tcombined.contains("create") ||
\t\t\t\t\tcombined.contains("submit") ||
\t\t\t\t\tcombined.contains("done") ||
\t\t\t\t\tcombined.contains("register") ||
\t\t\t\t\tcombined.contains("sign up") ||
\t\t\t\t\tcombined.contains("sign in") ||
\t\t\t\t\tcombined.contains("login") ||
\t\t\t\t\t"submit".equals(type)
\t\t\t\t) {
\t\t\t\t\tclickElementHard(element);
\t\t\t\t\tSystem.out.println("SAVE_HELPER_CLICK_DONE_BY_SCAN");
\t\t\t\t\treturn;
\t\t\t\t}
\t\t\t}
\t\t} catch (Exception e) {
\t\t\tlast = e;
\t\t}

\t\ttry {
\t\t\tWebElement active = driver.switchTo().activeElement();
\t\t\tactive.sendKeys(Keys.ENTER);
\t\t\tThread.sleep(1500);
\t\t\tSystem.out.println("SAVE_HELPER_DONE_BY_ENTER");
\t\t\treturn;
\t\t} catch (Exception e) {
\t\t\tlast = e;
\t\t}

\t\ttry {
\t\t\tWebElement form = driver.findElement(By.xpath("//form"));
\t\t\thighlight(form);
\t\t\tform.submit();
\t\t\tThread.sleep(1500);
\t\t\tSystem.out.println("SAVE_HELPER_DONE_BY_FORM_SUBMIT");
\t\t\treturn;
\t\t} catch (Exception e) {
\t\t\tlast = e;
\t\t}

\t\tSystem.out.println("SAVE_HELPER_FAILED: " + xpath);
\t\tif (last != null) throw last;
\t}

\tprivate void safeType(String xpath, String value) throws Exception {
\t\tif (xpath == null || xpath.trim().isEmpty()) return;

\t\tWebElement element = findVisibleElement(xpath);
\t\thighlight(element);

\t\ttry {
\t\t\telement.clear();
\t\t\telement.sendKeys(value);
\t\t} catch (Exception e) {
\t\t\tThread.sleep(500);
\t\t\telement.clear();
\t\t\telement.sendKeys(value);
\t\t}

\t\tThread.sleep(800);
\t}

\tprivate void safeSelect(String xpath, String value) throws Exception {
\t\tif (xpath == null || xpath.trim().isEmpty()) return;

\t\tWebElement element = findVisibleElement(xpath);
\t\thighlight(element);

\t\tSelect select = new Select(element);

\t\ttry {
\t\t\tselect.selectByVisibleText(value);
\t\t} catch (Exception e) {
\t\t\tboolean selected = false;

\t\t\tfor (WebElement option : select.getOptions()) {
\t\t\t\tString txt = option.getText().trim().toLowerCase();

\t\t\t\tif (txt.contains(value.trim().toLowerCase())) {
\t\t\t\t\toption.click();
\t\t\t\t\tselected = true;
\t\t\t\t\tbreak;
\t\t\t\t}
\t\t\t}

\t\t\tif (!selected && select.getOptions().size() > 1) {
\t\t\t\tselect.getOptions().get(1).click();
\t\t\t}
\t\t}

\t\tThread.sleep(800);
\t}
"""

    login_block = ""
    if login_config:
        login_block += "\t\ttry {\n"
        login_block += f"\t\t\tdriver.findElement(By.xpath(\"{escape_java_string(login_config['user_xpath'])}\")).clear();\n"
        login_block += f"\t\t\tdriver.findElement(By.xpath(\"{escape_java_string(login_config['user_xpath'])}\")).sendKeys(\"{escape_java_string(login_config['user_val'])}\");\n"

        last_input_xpath = login_config["user_xpath"]

        if login_config.get("pass_xpath"):
            login_block += f"\t\t\tdriver.findElement(By.xpath(\"{escape_java_string(login_config['pass_xpath'])}\")).clear();\n"
            login_block += f"\t\t\tdriver.findElement(By.xpath(\"{escape_java_string(login_config['pass_xpath'])}\")).sendKeys(\"{escape_java_string(login_config['pass_val'])}\");\n"
            last_input_xpath = login_config["pass_xpath"]

        if login_config.get("submit_type") == "enter":
            login_block += f"\t\t\tdriver.findElement(By.xpath(\"{escape_java_string(last_input_xpath)}\")).sendKeys(Keys.ENTER);\n"
        elif login_config.get("submit_xpath"):
            login_block += f"\t\t\tsafeClick(\"{escape_java_string(login_config['submit_xpath'])}\");\n"

        login_block += f"\t\t\tThread.sleep({wait_time});\n"
        login_block += "\t\t} catch (Exception e) {}\n"

    normal_code = f"""package tests;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;
import utils.DriverProvider;
import utils.Properties;
import utils.BasePageObject;

public class {class_name_normal} {{

\tprivate static WebDriver driver;
\tprivate static BasePageObject basePageObject;

{helper_methods}

\t@BeforeClass
\tpublic static void oneTimeSetUp() throws Exception {{
\t\tdriver = DriverProvider.getInstance().getDriver();
\t\tdriver.manage().window().maximize();
\t\tdriver.get(Properties.app_url);
\t\tbasePageObject = new BasePageObject(driver);
\t}}

\t@Before
\tpublic void setUp() throws Exception {{
\t\tThread.sleep({wait_time});
\t}}
"""

    coverage_code = f"""package tests;

import java.util.List;

import coverage.CodeCoverage;
import io.webfolder.cdp.session.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;
import utils.BasePageObject;
import utils.DriverProvider;
import utils.Properties;
import utils.SessionProvider;

public class {class_name_coverage} {{

\tprivate static WebDriver driver;
\tprivate static Session session;
\tprivate static CodeCoverage codeCoverage;
\tprivate static BasePageObject basePageObject;

{helper_methods}

\t@BeforeClass
\tpublic static void oneTimeSetUp() throws Exception {{
\t\tdriver = DriverProvider.getInstance().getDriver();
\t\tdriver.manage().window().maximize();
\t\tsession = SessionProvider.getInstance().createSession(driver);
\t\tcodeCoverage = new CodeCoverage(session);
\t\tdriver.get(Properties.app_url);
\t\tbasePageObject = new BasePageObject(driver);
\t}}

\t@Before
\tpublic void setUp() throws Exception {{
\t\tThread.sleep({wait_time});
\t}}
"""

    valid_scenarios = []
    for scenario in all_scenarios:
        cleaned = sanitize_scenario(scenario)
        if cleaned:
            valid_scenarios.append(cleaned)
    valid_scenarios = remove_exact_prefix_scenarios(valid_scenarios)

    for i, scenario in enumerate(valid_scenarios):
        test_method_name = f"test{i:03d}"

        reason = ""
        for st in scenario:
            if st.get("scenario_reason"):
                reason = st.get("scenario_reason")
                break

        normal_code += f"\n\t@Test()\n\tpublic void {test_method_name}() throws Exception {{\n"
        if reason:
            normal_code += f"\t\tSystem.out.println(\"SCENARIO_REASON: {escape_java_string(reason)}\");\n"
        normal_code += "\t\tdriver.get(Properties.app_url);\n"
        normal_code += f"\t\tThread.sleep({wait_time});\n"
        normal_code += login_block

        coverage_code += f"\n\t@Test()\n\tpublic void {test_method_name}() throws Exception {{\n"
        coverage_code += f"\t\tcodeCoverage.setTestCaseNameBeingExecuted(\"{test_method_name}\");\n"
        if reason:
            coverage_code += f"\t\tSystem.out.println(\"SCENARIO_REASON: {escape_java_string(reason)}\");\n"
        coverage_code += "\t\tdriver.get(Properties.app_url);\n"
        coverage_code += f"\t\tThread.sleep({wait_time});\n"
        coverage_code += login_block

        for step in scenario:
            safe_xpath = escape_java_string(step["xpath"])
            action = step.get("action", "click").lower()
            input_val = escape_java_string(step.get("input_value", ""))
            element_label = escape_java_string(step.get("element", ""))
            feature_signature = escape_java_string(step.get("feature_signature", ""))

            java_step = "\t\ttry {\n"
            java_step += f"\t\t\tSystem.out.println(\"STEP: {action.upper()} | {element_label} | {feature_signature}\");\n"

            if action == "input":
                java_step += f"\t\t\tsafeType(\"{safe_xpath}\", \"{input_val}\");\n"
            elif action == "select":
                java_step += f"\t\t\tsafeSelect(\"{safe_xpath}\", \"{input_val}\");\n"
            else:
                if is_commit_step(step):
                    java_step += f"\t\t\tsafeSaveButtonClick(\"{safe_xpath}\");\n"
                else:
                    java_step += f"\t\t\tsafeClick(\"{safe_xpath}\");\n"

            java_step += f"\t\t\tThread.sleep({wait_time});\n"
            java_step += (
                "\t\t} catch (Exception e) { "
                f"System.out.println(\"STEP_SKIPPED: {safe_xpath}\"); "
                "}\n"
            )

            normal_code += java_step
            coverage_code += java_step

        normal_code += "\t}\n"
        coverage_code += "\t}\n"

    normal_code += """
\t@AfterClass
\tpublic static void tearDown() throws Exception {
\t\ttry {
\t\t\tif (driver != null) driver.quit();
\t\t} catch (Exception e) {}
\t}
}
"""

    coverage_code += """
\t@After
\tpublic void saveIntermediateReport() throws Exception {
\t\ttry {
\t\t\tif (codeCoverage != null) {
\t\t\t\tcodeCoverage.saveCoverage(true);
\t\t\t\tcodeCoverage.resetCoveragePerTest();
\t\t\t}
\t\t} catch (Exception e) {
\t\t\tSystem.out.println("COVERAGE_SAVE_SKIPPED");
\t\t}
\t}

\t@AfterClass
\tpublic static void tearDown() throws Exception {
\t\ttry {
\t\t\tif (codeCoverage != null) {
\t\t\t\tcodeCoverage.saveCoverage(false);
\t\t\t}
\t\t} catch (Exception e) {
\t\t\tSystem.out.println("FINAL_COVERAGE_SAVE_SKIPPED");
\t\t}

\t\ttry {
\t\t\tif (session != null) session.close();
\t\t} catch (Exception e) {}

\t\ttry {
\t\t\tif (driver != null) driver.quit();
\t\t} catch (Exception e) {}
\t}
}
"""

    with open(f"{class_name_normal}.java", "w", encoding="utf-8") as f:
        f.write(normal_code)

    with open(f"{class_name_coverage}.java", "w", encoding="utf-8") as f:
        f.write(coverage_code)

    print(f"✅ SUCCESS! LLM-centric Dante-compatible Java tests generated. Valid scenarios: {len(valid_scenarios)}")