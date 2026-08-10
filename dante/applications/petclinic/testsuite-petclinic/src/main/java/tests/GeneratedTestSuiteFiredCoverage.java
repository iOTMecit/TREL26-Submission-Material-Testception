package tests;

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

public class GeneratedTestSuiteFiredCoverage {

	private static WebDriver driver;
	private static Session session;
	private static CodeCoverage codeCoverage;
	private static BasePageObject basePageObject;


	private void highlight(WebElement element) {
		try {
			((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView({block: 'center', inline: 'center'});" +
				"arguments[0].style.border='4px solid red';" +
				"arguments[0].style.backgroundColor='yellow';",
				element
			);
			Thread.sleep(700);
		} catch (Exception e) {}
	}

	private boolean isVisibleAndEnabled(WebElement element) {
		try {
			return element != null && element.isDisplayed() && element.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

	private String norm(String value) {
		if (value == null) return "";
		return value.trim().toLowerCase();
	}

	private void clickElementHard(WebElement element) throws Exception {
		highlight(element);

		try {
			element.click();
		} catch (Exception e1) {
			try {
				((org.openqa.selenium.JavascriptExecutor) driver)
					.executeScript("arguments[0].click();", element);
			} catch (Exception e2) {
				((org.openqa.selenium.JavascriptExecutor) driver)
					.executeScript(
						"var ev = new MouseEvent('click', {bubbles:true, cancelable:true, view:window}); arguments[0].dispatchEvent(ev);",
						element
					);
			}
		}

		Thread.sleep(1500);
	}
	private WebElement findVisibleElement(String xpath) throws Exception {
		if (xpath == null || xpath.trim().isEmpty()) {
			throw new RuntimeException("Empty xpath");
		}
		List<WebElement> elements = driver.findElements(By.xpath(xpath));
		if (elements == null || elements.isEmpty()) {
			throw new RuntimeException("Element not found: " + xpath);
		}
		// Modal/pop-up durumlarında genellikle son görünen element aktif olur.
		for (int i = elements.size() - 1; i >= 0; i--) {
			WebElement element = elements.get(i);
			if (isVisibleAndEnabled(element)) {
				return element;
			}
		}

		// Hiç visible yoksa Selenium'un hata vermesi yerine son elementi döndürüp
		// JS click/scroll fallback'e şans verelim.
		return elements.get(elements.size() - 1);
	}
	private void safeClick(String xpath) throws Exception {
	if (xpath == null || xpath.trim().isEmpty()) return;
		WebElement element = findVisibleElement(xpath);
		clickElementHard(element);
	}
	private void safeSaveButtonClick(String xpath) throws Exception {
		System.out.println("SAVE_HELPER_START: " + xpath);

		String[] directXpaths = new String[] {
			xpath,

			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'save')]",
			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'create')]",
			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'submit')]",
			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'done')]",
			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'register')]",
			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sign up')]",
			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sign in')]",
			"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'login')]",
			"//button[@type='submit']",
			"//input[@type='submit']",
			"(//form//button)[last()]",
		};

		Exception last = null;

		for (String candidate : directXpaths) {
			if (candidate == null || candidate.trim().isEmpty()) continue;

			try {
				List<WebElement> found = driver.findElements(By.xpath(candidate));
				System.out.println("SAVE_HELPER_XPATH: " + candidate + " count=" + found.size());

				for (WebElement element : found) {
					if (!isVisibleAndEnabled(element)) continue;

					String txt = norm(element.getText());
					String val = norm(element.getAttribute("value"));
					String aria = norm(element.getAttribute("aria-label"));
					String type = norm(element.getAttribute("type"));
					String cls = norm(element.getAttribute("class"));

					String combined = txt + " " + val + " " + aria + " " + type + " " + cls;
					System.out.println("SAVE_HELPER_CANDIDATE: " + combined);

					if (
						combined.contains("save") ||
						combined.contains("create") ||
						combined.contains("submit") ||
						combined.contains("done") ||
						combined.contains("register") ||
						combined.contains("sign up") ||
						combined.contains("sign in") ||
						combined.contains("login") ||
						"submit".equals(type)
					) {
						clickElementHard(element);
						System.out.println("SAVE_HELPER_CLICK_DONE");
						return;
					}
				}
			} catch (Exception e) {
				last = e;
			}
		}

		try {
			List<WebElement> all = driver.findElements(By.xpath("//button | //a | //input[@type='submit'] | //input[@type='button']"));
			System.out.println("SAVE_HELPER_SCAN_COUNT=" + all.size());

			for (WebElement element : all) {
				if (!isVisibleAndEnabled(element)) continue;

				String txt = norm(element.getText());
				String val = norm(element.getAttribute("value"));
				String aria = norm(element.getAttribute("aria-label"));
				String cls = norm(element.getAttribute("class"));
				String type = norm(element.getAttribute("type"));

				String combined = txt + " " + val + " " + aria + " " + cls + " " + type;
				System.out.println("SAVE_HELPER_SCAN_ELEMENT: " + combined);

				if (
					combined.contains("save") ||
					combined.contains("create") ||
					combined.contains("submit") ||
					combined.contains("done") ||
					combined.contains("register") ||
					combined.contains("sign up") ||
					combined.contains("sign in") ||
					combined.contains("login") ||
					"submit".equals(type)
				) {
					clickElementHard(element);
					System.out.println("SAVE_HELPER_CLICK_DONE_BY_SCAN");
					return;
				}
			}
		} catch (Exception e) {
			last = e;
		}

		try {
			WebElement active = driver.switchTo().activeElement();
			active.sendKeys(Keys.ENTER);
			Thread.sleep(1500);
			System.out.println("SAVE_HELPER_DONE_BY_ENTER");
			return;
		} catch (Exception e) {
			last = e;
		}

		try {
			WebElement form = driver.findElement(By.xpath("//form"));
			highlight(form);
			form.submit();
			Thread.sleep(1500);
			System.out.println("SAVE_HELPER_DONE_BY_FORM_SUBMIT");
			return;
		} catch (Exception e) {
			last = e;
		}

		System.out.println("SAVE_HELPER_FAILED: " + xpath);
		if (last != null) throw last;
	}

	private void safeType(String xpath, String value) throws Exception {
		if (xpath == null || xpath.trim().isEmpty()) return;

		WebElement element = findVisibleElement(xpath);
		highlight(element);

		try {
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			Thread.sleep(500);
			element.clear();
			element.sendKeys(value);
		}

		Thread.sleep(800);
	}

	private void safeSelect(String xpath, String value) throws Exception {
		if (xpath == null || xpath.trim().isEmpty()) return;

		WebElement element = findVisibleElement(xpath);
		highlight(element);

		Select select = new Select(element);

		try {
			select.selectByVisibleText(value);
		} catch (Exception e) {
			boolean selected = false;

			for (WebElement option : select.getOptions()) {
				String txt = option.getText().trim().toLowerCase();

				if (txt.contains(value.trim().toLowerCase())) {
					option.click();
					selected = true;
					break;
				}
			}

			if (!selected && select.getOptions().size() > 1) {
				select.getOptions().get(1).click();
			}
		}

		Thread.sleep(800);
	}


	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.manage().window().maximize();
		session = SessionProvider.getInstance().createSession(driver);
		codeCoverage = new CodeCoverage(session);
		driver.get("http://localhost:3000/petclinic/welcome");
		basePageObject = new BasePageObject(driver);
	}

	@Before
	public void setUp() throws Exception {
		Thread.sleep(250);
	}

	@Test()
	public void test000() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test000");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New Pet | add_or_open:add new pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add New Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add New Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='owner_name']:vimmqyki");
			safeType("//INPUT[@id = 'owner_name']", "vImMQyki");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Save Pet | commit:save pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Pet | crud_or_detail:edit pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='type1']:ipqnlihd");
			safeType("//INPUT[@id = 'type1']", "iPqnliHD");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'type1']"); }
		try {
			System.out.println("STEP: INPUT | owner_name | edge_replay:input://input[@id='owner_name']:mhjtiupj");
			safeType("//INPUT[@id = 'owner_name']", "mhJTIuPj");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT | birthDate | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Update Pet | commit:update pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Add Visit | add_or_open:add visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Visit')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='date']:2026/07/24");
			safeType("//INPUT[@name = 'date']", "2026/07/24");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'date']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='description']:rlm updated examination");
			safeType("//INPUT[@id = 'description']", "RLM updated examination");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'description']"); }
		try {
			System.out.println("STEP: CLICK | Add Visit | commit:add visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Visit')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Visit | crud_or_detail:edit visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Visit')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='date']:2026/07/10");
			safeType("//INPUT[@name = 'date']", "2026/07/10");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'date']"); }
		try {
			System.out.println("STEP: CLICK | Update Visit | commit:update visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Visit')]"); }
		try {
			System.out.println("STEP: CLICK | Delete Visit | crud_or_detail:delete visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete Visit')]"); }
	}

	@Test()
	public void test001() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test001");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New Pet | add_or_open:add new pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add New Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add New Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='owner_name']:vimmqyki");
			safeType("//INPUT[@id = 'owner_name']", "vImMQyki");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Save Pet | commit:save pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Pet | crud_or_detail:edit pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='type1']:ipqnlihd");
			safeType("//INPUT[@id = 'type1']", "iPqnliHD");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'type1']"); }
		try {
			System.out.println("STEP: INPUT | owner_name | edge_replay:input://input[@id='owner_name']:mhjtiupj");
			safeType("//INPUT[@id = 'owner_name']", "mhJTIuPj");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT | birthDate | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Update Pet | commit:update pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Add Visit | add_or_open:add visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Visit')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='date']:2026/07/24");
			safeType("//INPUT[@name = 'date']", "2026/07/24");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'date']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='description']:rlm updated examination");
			safeType("//INPUT[@id = 'description']", "RLM updated examination");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'description']"); }
		try {
			System.out.println("STEP: CLICK | Add Visit | commit:add visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Visit')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Visit | crud_or_detail:edit visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Visit')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
	}

	@Test()
	public void test002() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test002");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New Pet | add_or_open:add new pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add New Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add New Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='owner_name']:vimmqyki");
			safeType("//INPUT[@id = 'owner_name']", "vImMQyki");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Save Pet | commit:save pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Pet | crud_or_detail:edit pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='type1']:ipqnlihd");
			safeType("//INPUT[@id = 'type1']", "iPqnliHD");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'type1']"); }
		try {
			System.out.println("STEP: INPUT | owner_name | edge_replay:input://input[@id='owner_name']:mhjtiupj");
			safeType("//INPUT[@id = 'owner_name']", "mhJTIuPj");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT | birthDate | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Update Pet | commit:update pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Add Visit | add_or_open:add visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Visit')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='date']:2026/07/24");
			safeType("//INPUT[@name = 'date']", "2026/07/24");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'date']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='description']:rlm updated examination");
			safeType("//INPUT[@id = 'description']", "RLM updated examination");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'description']"); }
		try {
			System.out.println("STEP: CLICK | Add Visit | commit:add visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Visit')]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test003() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test003");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New Pet | add_or_open:add new pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add New Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add New Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='owner_name']:vimmqyki");
			safeType("//INPUT[@id = 'owner_name']", "vImMQyki");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Save Pet | commit:save pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Pet | crud_or_detail:edit pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='type1']:ipqnlihd");
			safeType("//INPUT[@id = 'type1']", "iPqnliHD");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'type1']"); }
		try {
			System.out.println("STEP: INPUT | owner_name | edge_replay:input://input[@id='owner_name']:mhjtiupj");
			safeType("//INPUT[@id = 'owner_name']", "mhJTIuPj");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT | birthDate | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Update Pet | commit:update pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Add Visit | add_or_open:add visit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Visit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Visit')]"); }
		try {
			System.out.println("STEP: CLICK | Open calendar | click:button:open calendar");
			safeClick("//BUTTON[@aria-label = 'Open calendar']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@aria-label = 'Open calendar']"); }
	}

	@Test()
	public void test004() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test004");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New Pet | add_or_open:add new pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add New Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add New Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='owner_name']:vimmqyki");
			safeType("//INPUT[@id = 'owner_name']", "vImMQyki");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Save Pet | commit:save pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Pet | crud_or_detail:edit pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='owner_name']:vimmqyki");
			safeType("//INPUT[@id = 'owner_name']", "vImMQyki");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | input:name:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | select:bootstraptype50291:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test005() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test005");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New Pet | add_or_open:add new pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add New Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add New Pet')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='owner_name']:vimmqyki");
			safeType("//INPUT[@id = 'owner_name']", "vImMQyki");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'owner_name']"); }
		try {
			System.out.println("STEP: INPUT | Name | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@name='birthdate']:2026/04/08");
			safeType("//INPUT[@name = 'birthDate']", "2026/04/08");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@name = 'birthDate']"); }
		try {
			System.out.println("STEP: SELECT | BootstrapType50291 | edge_replay:select://select[@id='type']:bootstraptype50291");
			safeSelect("//SELECT[@id = 'type']", "BootstrapType50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'type']"); }
		try {
			System.out.println("STEP: CLICK | Save Pet | commit:save pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
	}

	@Test()
	public void test006() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test006");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New Pet | add_or_open:add new pet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add New Pet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add New Pet')]"); }
		try {
			System.out.println("STEP: CLICK | Open calendar | click:button:open calendar");
			safeClick("//BUTTON[@aria-label = 'Open calendar']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@aria-label = 'Open calendar']"); }
	}

	@Test()
	public void test007() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test007");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Update Owner | commit:update owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update Owner')]"); }
		try {
			System.out.println("STEP: CLICK | < Back | click:button:< back");
			safeClick("//BUTTON[contains(normalize-space(.), '< Back')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), '< Back')]"); }
	}

	@Test()
	public void test008() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test008");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | RLMOwnerUpdated Updated50291 | crud_or_detail:rlmownerupdated updated50291");
			safeClick("//A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'RLMOwnerUpdated Updated50291')]"); }
		try {
			System.out.println("STEP: CLICK | Edit Owner | crud_or_detail:edit owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Owner')]"); }
		try {
			System.out.println("STEP: INPUT | Address | input:address:updated automation s");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address']"); }
		try {
			System.out.println("STEP: INPUT | City | input:city:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city']"); }
		try {
			System.out.println("STEP: INPUT | Telephone | input:telephone:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone']"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test009() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test009");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test010() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test010");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test011() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test011");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')]"); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName']"); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated50291");
			safeType("//INPUT[@id = 'lastName']", "Updated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName']"); }
		try {
			System.out.println("STEP: SELECT | Type | edge_replay:select://select[@id='specialties']:bootstrapspecialty50291");
			safeSelect("//SELECT[@id = 'specialties']", "BootstrapSpecialty50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'specialties']"); }
		try {
			System.out.println("STEP: CLICK | Save Vet | commit:save vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Vet')]"); }
	}

	@Test()
	public void test012() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test012");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test013() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test013");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test014() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test014");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes']"); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties']"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:new specialty name");
			safeType("//INPUT[@id = '0']", "New Specialty Name");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0']"); }
	}

	@Test()
	public void test015() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test015");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes']"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:new pet type");
			safeType("//INPUT[@id = '0']", "New Pet Type");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')]"); }
	}

	@Test()
	public void test016() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test016");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties']"); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save')]"); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')]"); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')]"); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete')]"); }
		try {
			System.out.println("STEP: CLICK | Home | click:button:home");
			safeClick("//BUTTON[contains(normalize-space(.), 'Home')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Home')]"); }
	}

	@Test()
	public void test017() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test017");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties']"); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save')]"); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')]"); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')]"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:newspecialtyname");
			safeType("//INPUT[@id = '0']", "NewSpecialtyName");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0']"); }
	}

	@Test()
	public void test018() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test018");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties']"); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save')]"); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')]"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test019() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test019");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties']"); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save')]"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:ssbjornh");
			safeType("//INPUT[@id = '0']", "ssbJorNH");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0']"); }
	}

	@Test()
	public void test020() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test020");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties']"); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')]"); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:petupdated50291");
			safeType("//INPUT[@id = 'name']", "PetUpdated50291");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name']"); }
	}

	@Test()
	public void test021() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test021");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test022() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test022");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test023() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test023");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test024() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test024");
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')]"); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test025() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test025");
		System.out.println("SCENARIO_REASON: Exploring the navigation options to find a meaningful action for the next test.");
		driver.get("http://localhost:3000/petclinic/welcome");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
	}

	@After
	public void saveIntermediateReport() throws Exception {
		try {
			if (codeCoverage != null) {
				codeCoverage.saveCoverage(true);
				codeCoverage.resetCoveragePerTest();
			}
		} catch (Exception e) {
			System.out.println("COVERAGE_SAVE_SKIPPED");
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		try {
			if (codeCoverage != null) {
				codeCoverage.saveCoverage(false);
			}
		} catch (Exception e) {
			System.out.println("FINAL_COVERAGE_SAVE_SKIPPED");
		}

		try {
			if (session != null) session.close();
		} catch (Exception e) {}

		try {
			if (driver != null) driver.quit();
		} catch (Exception e) {}
	}
}
