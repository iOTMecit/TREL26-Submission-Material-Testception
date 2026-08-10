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

public class GeneratedTestSuiteCheckedCoverage {

	private static WebDriver driver;
	private static Session session;
	private static CodeCoverage codeCoverage;
	private static BasePageObject basePageObject;

	private void highlight(WebElement element) {
		try {
			((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView({block: 'center', inline: 'center'});"
							+ "arguments[0].style.border='4px solid red';"
							+ "arguments[0].style.backgroundColor='yellow';",
					element);
			Thread.sleep(700);
		} catch (Exception e) {
		}
	}

	private boolean isVisibleAndEnabled(WebElement element) {
		try {
			return element != null && element.isDisplayed()
					&& element.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

	private String norm(String value) {
		if (value == null)
			return "";
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
								element);
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
		for (int i = elements.size() - 1; i >= 0; i--) {
			WebElement element = elements.get(i);
			if (isVisibleAndEnabled(element)) {
				return element;
			}
		}
		return elements.get(elements.size() - 1);
	}
	private void safeClick(String xpath) throws Exception {
		if (xpath == null || xpath.trim().isEmpty())
			return;
		WebElement element = findVisibleElement(xpath);
		clickElementHard(element);
	}
	private void safeSaveButtonClick(String xpath) throws Exception {
		System.out.println("SAVE_HELPER_START: " + xpath);
		String[] directXpaths = new String[]{
				xpath,
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'save')]",
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'create')]",
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'submit')]",
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'done')]",
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'register')]",
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sign up')]",
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sign in')]",
				"//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'login')]",
				"//button[@type='submit']", "//input[@type='submit']",
				"(//form//button)[last()]"};
		Exception last = null;
		for (String candidate : directXpaths) {
			if (candidate == null || candidate.trim().isEmpty())
				continue;
			try {
				List<WebElement> found = driver.findElements(By
						.xpath(candidate));
				System.out.println("SAVE_HELPER_XPATH: " + candidate
						+ " count=" + found.size());
				for (WebElement element : found) {
					if (!isVisibleAndEnabled(element))
						continue;
					String txt = norm(element.getText());
					String val = norm(element.getAttribute("value"));
					String aria = norm(element.getAttribute("aria-label"));
					String type = norm(element.getAttribute("type"));
					String cls = norm(element.getAttribute("class"));
					String combined = txt + " " + val + " " + aria + " " + type
							+ " " + cls;
					System.out.println("SAVE_HELPER_CANDIDATE: " + combined);
					if (combined.contains("save")
							|| combined.contains("create")
							|| combined.contains("submit")
							|| combined.contains("done")
							|| combined.contains("register")
							|| combined.contains("sign up")
							|| combined.contains("sign in")
							|| combined.contains("login")
							|| "submit".equals(type)) {
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
			List<WebElement> all = driver
					.findElements(By
							.xpath("//button | //a | //input[@type='submit'] | //input[@type='button']"));
			System.out.println("SAVE_HELPER_SCAN_COUNT=" + all.size());
			for (WebElement element : all) {
				if (!isVisibleAndEnabled(element))
					continue;
				String txt = norm(element.getText());
				String val = norm(element.getAttribute("value"));
				String aria = norm(element.getAttribute("aria-label"));
				String cls = norm(element.getAttribute("class"));
				String type = norm(element.getAttribute("type"));
				String combined = txt + " " + val + " " + aria + " " + cls
						+ " " + type;
				System.out.println("SAVE_HELPER_SCAN_ELEMENT: " + combined);
				if (combined.contains("save") || combined.contains("create")
						|| combined.contains("submit")
						|| combined.contains("done")
						|| combined.contains("register")
						|| combined.contains("sign up")
						|| combined.contains("sign in")
						|| combined.contains("login") || "submit".equals(type)) {
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
		if (last != null)
			throw last;
	}

	private void safeType(String xpath, String value) throws Exception {
		if (xpath == null || xpath.trim().isEmpty())
			return;
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
		if (xpath == null || xpath.trim().isEmpty())
			return;
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
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Before
	public void setUp() throws Exception {
		Thread.sleep(250);
	}

	@Test()
	public void test000() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test000");
		System.out
				.println("SCENARIO_REASON: Navigating to the event creation page to initiate the process of creating a new event.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Source Code | click:a:source code");
			safeClick("//A[contains(normalize-space(.), 'Source Code')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source Code')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Cowbell Labs | click:a:cowbell labs");
			safeClick("//A[contains(normalize-space(.), 'Cowbell Labs')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Cowbell Labs')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[contains(normalize-space(.), 'New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | input:example: trip to barcelona:qa test event");
			safeType("//INPUT[@placeholder = 'Example: Trip to Barcelona']",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Example: Trip to Barcelona']");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/NAV[1]/DIV[1]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/NAV[1]/DIV[1]/BUTTON[2]");
		}
	}

	@Test()
	public void test001() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test001");
		System.out
				.println("SCENARIO_REASON: Navigating to the Create New Event page to fill out the event creation form.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out.println("STEP: CLICK | github | click:a:github");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[2]/SECTION[2]/DIV[2]/DIV[3]/P[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[2]/SECTION[2]/DIV[2]/DIV[3]/P[1]/A[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | SplittyPie Source Code | click:a:splittypie source code");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[2]/SECTION[3]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[2]/SECTION[3]/A[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Author Cowbell-Labs Page | click:a:author cowbell-labs page");
			safeClick("//A[@title = 'Author Cowbell-Labs Page']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Author Cowbell-Labs Page']");
		}
		try {
			System.out
					.println("STEP: CLICK | Facebook Page | click:a:facebook page");
			safeClick("//A[@title = 'Facebook Page']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Facebook Page']");
		}
	}

	@Test()
	public void test002() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test002");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after previous attempts to access the event creation page.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Twitter Page | click:a:twitter page");
			safeClick("//A[@title = 'Twitter Page']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Twitter Page']");
		}
	}

	@Test()
	public void test003() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test003");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the app's features.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out.println("STEP: CLICK | About | click:a:about");
			safeClick("//A[@title = 'About']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'About']");
		}
		try {
			System.out.println("STEP: CLICK | Features | click:a:features");
			safeClick("//A[@title = 'Features']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Features']");
		}
	}

	@Test()
	public void test004() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test004");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[contains(normalize-space(.), 'New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'New Event')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
	}

	@Test()
	public void test005() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test005");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[contains(normalize-space(.), 'New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'New Event')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
	}

	@Test()
	public void test006() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test006");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[contains(normalize-space(.), 'New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'New Event')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | input:example: trip to barcelona:qa test event");
			safeType("//INPUT[@placeholder = 'Example: Trip to Barcelona']",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Example: Trip to Barcelona']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
	}

	@Test()
	public void test007() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test007");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[contains(normalize-space(.), 'New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'New Event')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
	}

	@Test()
	public void test008() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test008");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]");
		}
	}

	@Test()
	public void test009() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test009");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
	}

	@Test()
	public void test010() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test010");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
	}

	@Test()
	public void test011() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test011");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | input:example: trip to barcelona:qa test event");
			safeType("//INPUT[@placeholder = 'Example: Trip to Barcelona']",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Example: Trip to Barcelona']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
	}

	@Test()
	public void test012() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test012");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Source Code | click:a:source code");
			safeClick("//A[contains(normalize-space(.), 'Source Code')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source Code')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Cowbell Labs | click:a:cowbell labs");
			safeClick("//A[contains(normalize-space(.), 'Cowbell Labs')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Cowbell Labs')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Overview | crud_or_detail:overview");
			safeClick("//A[@title = 'Overview']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Overview']");
		}
		try {
			System.out
					.println("STEP: CLICK | Transactions | crud_or_detail:transactions");
			safeClick("//A[@title = 'Transactions']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Transactions']");
		}
		try {
			System.out
					.println("STEP: CLICK | Add your first transaction | add_or_open:add your first transaction");
			safeClick("//DIV[contains(normalize-space(.), 'Add your first transaction')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Add your first transaction')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | input:example: trip to barcelona:qa test event");
			safeType("//INPUT[@placeholder = 'Example: Trip to Barcelona']",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Example: Trip to Barcelona']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
	}

	@Test()
	public void test013() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test013");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Source Code | click:a:source code");
			safeClick("//A[contains(normalize-space(.), 'Source Code')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source Code')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Cowbell Labs | click:a:cowbell labs");
			safeClick("//A[contains(normalize-space(.), 'Cowbell Labs')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Cowbell Labs')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Overview | crud_or_detail:overview");
			safeClick("//A[@title = 'Overview']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Overview']");
		}
		try {
			System.out
					.println("STEP: CLICK | Transactions | crud_or_detail:transactions");
			safeClick("//A[@title = 'Transactions']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Transactions']");
		}
		try {
			System.out
					.println("STEP: CLICK | Add your first transaction | add_or_open:add your first transaction");
			safeClick("//DIV[contains(normalize-space(.), 'Add your first transaction')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Add your first transaction')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
	}

	@Test()
	public void test014() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test014");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Source Code | click:a:source code");
			safeClick("//A[contains(normalize-space(.), 'Source Code')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source Code')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Cowbell Labs | click:a:cowbell labs");
			safeClick("//A[contains(normalize-space(.), 'Cowbell Labs')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Cowbell Labs')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Overview | crud_or_detail:overview");
			safeClick("//A[@title = 'Overview']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Overview']");
		}
		try {
			System.out
					.println("STEP: CLICK | Transactions | crud_or_detail:transactions");
			safeClick("//A[@title = 'Transactions']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Transactions']");
		}
		try {
			System.out
					.println("STEP: CLICK | Add your first transaction | add_or_open:add your first transaction");
			safeClick("//DIV[contains(normalize-space(.), 'Add your first transaction')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Add your first transaction')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
	}

	@Test()
	public void test015() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test015");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Source Code | click:a:source code");
			safeClick("//A[contains(normalize-space(.), 'Source Code')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source Code')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Cowbell Labs | click:a:cowbell labs");
			safeClick("//A[contains(normalize-space(.), 'Cowbell Labs')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Cowbell Labs')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Overview | crud_or_detail:overview");
			safeClick("//A[@title = 'Overview']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Overview']");
		}
		try {
			System.out
					.println("STEP: CLICK | Transactions | crud_or_detail:transactions");
			safeClick("//A[@title = 'Transactions']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Transactions']");
		}
		try {
			System.out
					.println("STEP: CLICK | Add your first transaction | add_or_open:add your first transaction");
			safeClick("//DIV[contains(normalize-space(.), 'Add your first transaction')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Add your first transaction')]");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | input:example: trip to barcelona:qa test event");
			safeType("//INPUT[@placeholder = 'Example: Trip to Barcelona']",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Example: Trip to Barcelona']");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
	}

	@Test()
	public void test016() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test016");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Source Code | click:a:source code");
			safeClick("//A[contains(normalize-space(.), 'Source Code')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source Code')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Cowbell Labs | click:a:cowbell labs");
			safeClick("//A[contains(normalize-space(.), 'Cowbell Labs')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Cowbell Labs')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Overview | crud_or_detail:overview");
			safeClick("//A[@title = 'Overview']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Overview']");
		}
		try {
			System.out
					.println("STEP: CLICK | Transactions | crud_or_detail:transactions");
			safeClick("//A[@title = 'Transactions']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Transactions']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT Add New Event | add_or_open:utyocvdt add new event");
			safeClick("//DIV[contains(normalize-space(.), 'UtYOCVDT Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'UtYOCVDT Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Share With Others | crud_or_detail:share with others");
			safeClick("//BUTTON[@aria-label = 'Share With Others']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@aria-label = 'Share With Others']");
		}
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//A[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Edit')]");
		}
	}

	@Test()
	public void test017() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test017");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/SPAN[1]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Source Code | click:a:source code");
			safeClick("//A[contains(normalize-space(.), 'Source Code')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source Code')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Cowbell Labs | click:a:cowbell labs");
			safeClick("//A[contains(normalize-space(.), 'Cowbell Labs')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Cowbell Labs')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Overview | crud_or_detail:overview");
			safeClick("//A[@title = 'Overview']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Overview']");
		}
		try {
			System.out
					.println("STEP: CLICK | Transactions | crud_or_detail:transactions");
			safeClick("//A[@title = 'Transactions']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Transactions']");
		}
		try {
			System.out
					.println("STEP: CLICK | Viewing as ExhyXceu | click:button:viewing as exhyxceu");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[3]/NAV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[3]/NAV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/BUTTON[1]");
		}
		try {
			System.out.println("STEP: CLICK | rOjKVcnk | click:a:rojkvcnk");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[3]/NAV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[3]/NAV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
		}
		try {
			System.out.println("STEP: CLICK | rOjKVcnk | click:a:rojkvcnk");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[3]/MAIN[1]/DIV[1]/NAV[1]/DIV[1]/DIV[1]/UL[1]/LI[3]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[3]/MAIN[1]/DIV[1]/NAV[1]/DIV[1]/DIV[1]/UL[1]/LI[3]/A[1]");
		}
	}

	@Test()
	public void test018() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test018");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | UtYOCVDT | click:button:utyocvdt");
			safeClick("//BUTTON[@id = 'dropDownEvents']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[@id = 'dropDownEvents']");
		}
		try {
			System.out.println("STEP: CLICK | Share | crud_or_detail:share");
			safeClick("//BUTTON[contains(normalize-space(.), 'Share')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Share')]");
		}
		try {
			System.out
					.println("STEP: CLICK | Edit Event | crud_or_detail:edit event");
			safeClick("//A[@title = 'Edit Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out.println("STEP_SKIPPED: //A[@title = 'Edit Event']");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
	}

	@Test()
	public void test019() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test019");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK |  | click:button:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Viewing as ExhyXceu Switch user to rOjKVcnk | click:div:viewing as exhyxceu switch user to rojkvcnk");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
	}

	@Test()
	public void test020() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test020");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Transaction | add_or_open:add new transaction");
			safeClick("//A[@title = 'Add New Transaction']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Add New Transaction']");
		}
		try {
			System.out
					.println("STEP: CLICK | Add New Event | add_or_open:add new event");
			safeClick("//A[contains(normalize-space(.), 'Add New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Add New Event')]");
		}
	}

	@Test()
	public void test021() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test021");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | input:example: trip to barcelona:qa test event");
			safeType("//INPUT[@placeholder = 'Example: Trip to Barcelona']",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Example: Trip to Barcelona']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
	}

	@Test()
	public void test022() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test022");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
	}

	@Test()
	public void test023() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test023");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: CLICK | Add Participant | add_or_open:add participant");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Participant')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Participant')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | input:your friend's name:bob");
			safeType("//INPUT[@placeholder = \"Your friend's name\"]", "Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = \"Your friend's name\"]");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | select:select currency... united states dollar (usd) euro (eur) pound sterling (gbp) polish złoty (pln) swiss franc (chf) czech koruna (czk) croatian kuna (hrk) romanian leu (ron) bulgari:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
	}

	@Test()
	public void test024() throws Exception {
		codeCoverage.setTestCaseNameBeingExecuted("test024");
		System.out
				.println("SCENARIO_REASON: Navigating to create a new event after exploring the source code and other pages.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Source | click:a:source");
			safeClick("//A[contains(normalize-space(.), 'Source')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Source')]");
		}
		try {
			System.out
					.println("STEP: CLICK | New Event | add_or_open:new event");
			safeClick("//A[@title = 'Create New Event']");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[@title = 'Create New Event']");
		}
		try {
			System.out
					.println("STEP: CLICK | Create New Event | add_or_open:create new event");
			safeClick("//A[contains(normalize-space(.), 'Create New Event')]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Create New Event')]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | input:your name:alice");
			safeType("//INPUT[@placeholder = 'Your name']", "Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: //INPUT[@placeholder = 'Your name']");
		}
		try {
			System.out
					.println("STEP: SELECT | Select currency... United States dollar (USD) Euro (EUR) Pound sterling (GBP) Polish złoty (PLN) Swiss franc (CHF) Czech koruna (CZK) Croatian kuna (HRK) Romanian leu (RON) Bulgari | edge_replay:select:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[2]/div[1]/select[1]:euro (eur)");
			safeSelect(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]",
					"Euro (EUR)");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SELECT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Example: Trip to Barcelona | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]:qa test event");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]",
					"QA Test Event");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[1]/div[1]/div[1]/input[1]:alice");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]",
					"Alice");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[1]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out
					.println("STEP: INPUT | Your friend's name | edge_replay:input:/html[1]/body[1]/div[1]/div[3]/main[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[3]/ul[1]/li[2]/div[1]/div[1]/input[1]:bob");
			safeType(
					"/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]",
					"Bob");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[1]/DIV[1]/DIV[3]/UL[1]/LI[2]/DIV[1]/DIV[1]/INPUT[1]");
		}
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeSaveButtonClick("/HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) {
			System.out
					.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[3]/MAIN[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/BUTTON[1]");
		}
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
			if (session != null)
				session.close();
		} catch (Exception e) {
		}
		try {
			if (driver != null)
				driver.quit();
		} catch (Exception e) {
		}
	}
}