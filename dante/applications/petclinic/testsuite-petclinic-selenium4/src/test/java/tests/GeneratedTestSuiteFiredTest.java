package tests;

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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.HasCdp;
import org.junit.Rule;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import coverage.Selenium4JsCoverage;

public class GeneratedTestSuiteFiredTest {

	private static WebDriver driver;
	private static Selenium4JsCoverage jsCoverage;
	private static boolean driverBroken = false;
	private static int testsStarted = 0;
	private static final int BROWSER_RECYCLE_EVERY = browserRecycleEvery();

	private static int browserRecycleEvery() {
		String raw = System.getenv().getOrDefault(
			"TESTCEPTION_BROWSER_RECYCLE_EVERY",
			"1"
		);
		try {
			int value = Integer.parseInt(raw.trim());
			return Math.max(0, value);
		} catch (Exception e) {
			return 1;
		}
	}

	private static WebDriver createChromeDriver() {
		ChromeOptions options = new ChromeOptions();
		boolean headless = Boolean.parseBoolean(
			System.getenv().getOrDefault(
				"TESTCEPTION_HEADLESS",
				"false")
		);

		if (headless) {
			options.addArguments("--headless=new");
		}

		options.addArguments("--remote-allow-origins=*");
		WebDriver newDriver = new ChromeDriver(options);
		newDriver.manage().window().maximize();
		return newDriver;
	}

	private static boolean isFatalDriverError(Throwable error) {
		Throwable current = error;
		while (current != null) {
			if (current instanceof org.openqa.selenium.TimeoutException
					|| current instanceof org.openqa.selenium.NoSuchSessionException) {
				return true;
			}

			String className = current.getClass().getName();
			String message = String.valueOf(current.getMessage()).toLowerCase();
			if (className.contains("UnreachableBrowserException")
					|| message.contains("java.util.concurrent.timeoutexception")
					|| message.contains("chrome not reachable")
					|| message.contains("disconnected")
					|| message.contains("invalid session id")) {
				return true;
			}
			current = current.getCause();
		}
		return false;
	}

	private static void markDriverBroken(Throwable error) {
		if (!isFatalDriverError(error)) return;
		driverBroken = true;
		System.out.println(
			"SELENIUM4_DRIVER_MARKED_BROKEN | "
			+ error.getClass().getSimpleName()
			+ " | "
			+ String.valueOf(error.getMessage())
		);
	}

	private static void recycleBrowser(String reason) {
		System.out.println(
			"SELENIUM4_BROWSER_RECYCLE_START | reason=" + reason
			+ " | testsStarted=" + testsStarted
		);

		boolean oldDriverBroken = driverBroken;

		if (!oldDriverBroken && jsCoverage != null) {
			jsCoverage.prepareForBrowserRecycle();
		}

		WebDriver oldDriver = driver;
		driver = null;

		if (oldDriver != null && !oldDriverBroken) {
			try {
				oldDriver.quit();
			} catch (Exception e) {
				System.out.println(
					"SELENIUM4_OLD_DRIVER_QUIT_WARNING | " + e
				);
			}
		} else if (oldDriverBroken) {
			System.out.println(
				"SELENIUM4_OLD_DRIVER_QUIT_SKIPPED | reason=DRIVER_UNRESPONSIVE"
			);
		}

		driver = createChromeDriver();

		if (!(driver instanceof HasCdp)) {
			throw new IllegalStateException(
				"Fresh Chrome driver does not implement HasCdp"
			);
		}

		jsCoverage.rebindToNewDriver((HasCdp) driver);
		driverBroken = false;

		System.out.println(
			"SELENIUM4_BROWSER_RECYCLE_DONE | reason=" + reason
		);
	}

	private static void ensureDriverAvailableForStep() throws Exception {
		if (driverBroken) {
			throw new RuntimeException(
				"Driver session already marked broken; step skipped without WebDriver call"
			);
		}
	}

	@Rule
	public final TestWatcher coverageWatcher = new TestWatcher() {
		private String name(Description description) {
			return description.getClassName()
				+ "#"
				+ description.getMethodName();
		}

		@Override
		protected void starting(Description description) {
			/*
			 * The browser created in @BeforeClass is fresh for test000.
			 * With the default interval=1, every later test recycles the
			 * previous browser before coverage begins, so each JUnit test
			 * executes in its own ChromeDriver/CDP session.
			 */
			boolean batchRecycle = BROWSER_RECYCLE_EVERY > 0
				&& testsStarted > 0
				&& testsStarted % BROWSER_RECYCLE_EVERY == 0;

			if (driverBroken || batchRecycle) {
				recycleBrowser(driverBroken ? "DRIVER_RECOVERY" : "PER_TEST_ISOLATION");
			}

			try {
				jsCoverage.beginTest(name(description));
			} catch (RuntimeException coverageStartError) {
				driverBroken = true;
				recycleBrowser("COVERAGE_START_RECOVERY");
				jsCoverage.beginTest(name(description));
			}

			testsStarted++;
		}

		@Override
		protected void succeeded(Description description) {
			String testName = name(description);
			if (driverBroken) {
				jsCoverage.abandonTestAfterDriverFailure(
					testName,
					"PASSED_DRIVER_FAILURE"
				);
				return;
			}

			try {
				jsCoverage.endTest(testName, true);
			} catch (RuntimeException coverageError) {
				driverBroken = true;
				jsCoverage.abandonTestAfterDriverFailure(
					testName,
					"PASSED_COVERAGE_ERROR"
				);
				System.out.println(
					"SELENIUM4_COVERAGE_ERROR_RECOVERABLE | "
					+ testName + " | " + coverageError
				);
			}
		}

		@Override
		protected void failed(
				Throwable error,
				Description description) {
			String testName = name(description);
			markDriverBroken(error);

			if (driverBroken) {
				jsCoverage.abandonTestAfterDriverFailure(
					testName,
					"FAILED_DRIVER_FAILURE"
				);
				return;
			}

			try {
				jsCoverage.endTest(testName, false);
			} catch (RuntimeException coverageError) {
				driverBroken = true;
				jsCoverage.abandonTestAfterDriverFailure(
					testName,
					"FAILED_COVERAGE_ERROR"
				);
				error.addSuppressed(coverageError);
			}
		}

		@Override
		protected void skipped(
				AssumptionViolatedException error,
				Description description) {
			jsCoverage.abortTest(name(description), "SKIPPED");
		}
	};


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
		ensureDriverAvailableForStep();
	if (xpath == null || xpath.trim().isEmpty()) return;
		WebElement element = findVisibleElement(xpath);
		clickElementHard(element);
	}
	private void safeSaveButtonClick(String xpath) throws Exception {
		ensureDriverAvailableForStep();
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
		ensureDriverAvailableForStep();
		if (xpath == null || xpath.trim().isEmpty()) return;

		WebElement element = findVisibleElement(xpath);
		String readOnly = element.getAttribute("readonly");
		String disabled = element.getAttribute("disabled");
		String ariaReadOnly = element.getAttribute("aria-readonly");

		if (
			!element.isDisplayed()
			|| !element.isEnabled()
			|| readOnly != null
			|| disabled != null
			|| "true".equalsIgnoreCase(ariaReadOnly)
		) {
			throw new org.openqa.selenium.InvalidElementStateException(
				"Element is not editable: " + xpath
			);
		}
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
		ensureDriverAvailableForStep();
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
		driver = createChromeDriver();

		if (!(driver instanceof HasCdp)) {
			throw new IllegalStateException(
				"Active Chrome driver does not implement HasCdp"
			);
		}

		jsCoverage = Selenium4JsCoverage.fromEnvironment(
			(HasCdp) driver
		);
		jsCoverage.start();
		System.out.println(
			"SELENIUM4_BROWSER_RECYCLE_EVERY=" + BROWSER_RECYCLE_EVERY
		);
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/");
	}

	@Before
	public void setUp() throws Exception {
		Thread.sleep(250);
	}

	@Test()
	public void test000() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated91092");
			safeType("//INPUT[@id = 'lastName']", "Updated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Address | edge_replay:input://input[@id='address']:updated automation street");
			safeType("//INPUT[@id = 'address']", "Updated Automation Street");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'address'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | City | edge_replay:input://input[@id='city']:manisa");
			safeType("//INPUT[@id = 'city']", "Manisa");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'city'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Telephone | edge_replay:input://input[@id='telephone']:5551234567");
			safeType("//INPUT[@id = 'telephone']", "5551234567");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'telephone'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Owner | commit:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Owner | add_or_open:add owner");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Owner')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Owner')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test001() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated91092");
			safeType("//INPUT[@id = 'lastName']", "Updated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: SELECT | Type | edge_replay:select://select[@id='specialties']:bootstrapspecialty91092");
			safeSelect("//SELECT[@id = 'specialties']", "BootstrapSpecialty91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save Vet | commit:save vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete Vet | crud_or_detail:delete vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test002() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated91092");
			safeType("//INPUT[@id = 'lastName']", "Updated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: SELECT | Type | edge_replay:select://select[@id='specialties']:bootstrapspecialty91092");
			safeSelect("//SELECT[@id = 'specialties']", "BootstrapSpecialty91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save Vet | commit:save vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test003() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:newspecialty");
			safeType("//INPUT[@id = 'name']", "NewSpecialty");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | crud_or_detail:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test004() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test005() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:specialtyname1");
			safeType("//INPUT[@id = '0']", "SpecialtyName1");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:specialtyname2");
			safeType("//INPUT[@id = '1']", "SpecialtyName2");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:specialtyname3");
			safeType("//INPUT[@id = '2']", "SpecialtyName3");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test006() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:newspecialtyname");
			safeType("//INPUT[@id = 'name']", "NewSpecialtyName");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | crud_or_detail:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test007() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:newspecialtyname");
			safeType("//INPUT[@id = '0']", "NewSpecialtyName");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:specialtydescription");
			safeType("//INPUT[@id = '1']", "SpecialtyDescription");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:additionalinfo");
			safeType("//INPUT[@id = '2']", "AdditionalInfo");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='3']:extradetails");
			safeType("//INPUT[@id = '3']", "ExtraDetails");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '3'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test008() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test009() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:gigaoghc");
			safeType("//INPUT[@id = '0']", "gigaoghC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | commit:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:eiiqyxev");
			safeType("//INPUT[@id = '0']", "EiiqyXEV");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test010() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:gigaoghc");
			safeType("//INPUT[@id = '0']", "gigaoghC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | commit:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:eiiqyxev");
			safeType("//INPUT[@id = '0']", "EiiqyXEV");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:newspecialtyname");
			safeType("//INPUT[@id = '0']", "NewSpecialtyName");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:newspecialtydescript");
			safeType("//INPUT[@id = '1']", "NewSpecialtyDescription");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test011() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:gigaoghc");
			safeType("//INPUT[@id = '0']", "gigaoghC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | commit:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:newspecialtyname");
			safeType("//INPUT[@id = '0']", "NewSpecialtyName");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:new specialty descri");
			safeType("//INPUT[@id = 'name']", "New Specialty Description");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test012() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test013() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:new specialty name");
			safeType("//INPUT[@id = '0']", "New Specialty Name");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:specialty descriptio");
			safeType("//INPUT[@id = '1']", "Specialty Description");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:additional info");
			safeType("//INPUT[@id = '2']", "Additional Info");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='3']:more details");
			safeType("//INPUT[@id = '3']", "More Details");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '3'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test014() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:new specialty name");
			safeType("//INPUT[@id = '0']", "New Specialty Name");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:specialty descriptio");
			safeType("//INPUT[@id = '1']", "Specialty Description");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:additional info");
			safeType("//INPUT[@id = '2']", "Additional Info");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='3']:more details");
			safeType("//INPUT[@id = '3']", "More Details");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '3'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test015() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:new specialty name");
			safeType("//INPUT[@id = '0']", "New Specialty Name");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:specialty descriptio");
			safeType("//INPUT[@id = '1']", "Specialty Description");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:additional info");
			safeType("//INPUT[@id = '2']", "Additional Info");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='3']:more details");
			safeType("//INPUT[@id = '3']", "More Details");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '3'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:new pet type");
			safeType("//INPUT[@id = '0']", "New Pet Type");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test016() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:new specialty name");
			safeType("//INPUT[@id = '0']", "New Specialty Name");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:specialty descriptio");
			safeType("//INPUT[@id = '1']", "Specialty Description");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:additional info");
			safeType("//INPUT[@id = '2']", "Additional Info");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='3']:more details");
			safeType("//INPUT[@id = '3']", "More Details");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '3'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test017() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | commit:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Home | click:button:home");
			safeClick("//BUTTON[contains(normalize-space(.), 'Home')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Home')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test018() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save | commit:save");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test019() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:jgoyoytq");
			safeType("//INPUT[@id = '0']", "JgOYOyTq");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test020() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test021() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Pet Types | crud_or_detail:pet types");
			safeClick("//A[@title = 'pettypes']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'pettypes'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:zkcqqnvc");
			safeType("//INPUT[@id = '0']", "ZkcQQNvC");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test022() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:fgluxind");
			safeType("//INPUT[@id = '0']", "FgluXINd");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:hrbdrlbl");
			safeType("//INPUT[@id = '1']", "hRbDRlBl");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:newspecialtyname");
			safeType("//INPUT[@id = 'name']", "NewSpecialtyName");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | crud_or_detail:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test023() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[2]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test024() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input://input[@id='name']:petupdated91092");
			safeType("//INPUT[@id = 'name']", "PetUpdated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Update | commit:update");
			safeClick("//BUTTON[contains(normalize-space(.), 'Update')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Update')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:kgqknyws");
			safeType("//INPUT[@id = '0']", "KgqknYWs");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:xoqlyrau");
			safeType("//INPUT[@id = '1']", "xoQLyRaU");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:xzszbbxk");
			safeType("//INPUT[@id = '2']", "XzSzBBxk");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test025() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit | crud_or_detail:edit");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='name']:newspecialtyname");
			safeType("//INPUT[@id = 'name']", "NewSpecialtyName");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'name'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test026() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete | crud_or_detail:delete");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-SPECIALTY-LIST[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/BUTTON[2] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test027() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Specialties | click:a:specialties");
			safeClick("//A[@title = 'specialties']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[@title = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='0']:hlsajaol");
			safeType("//INPUT[@id = '0']", "hlsAJAOl");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '0'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='1']:iquhcuxh");
			safeType("//INPUT[@id = '1']", "IquHCUxH");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '1'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT |  | input://input[@id='2']:memqfsqm");
			safeType("//INPUT[@id = '2']", "mEmQfsQM");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = '2'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add | add_or_open:add");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add')] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test028() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated91092");
			safeType("//INPUT[@id = 'lastName']", "Updated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: SELECT | PetUpdated91092 PetUpdated91092 | edge_replay:select://select[@id='specialties']:bootstrapspecialty91092");
			safeSelect("//SELECT[@id = 'specialties']", "BootstrapSpecialty91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save Vet | commit:save vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit Vet | crud_or_detail:edit vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete Vet | crud_or_detail:delete vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test029() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated91092");
			safeType("//INPUT[@id = 'lastName']", "Updated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: SELECT | PetUpdated91092 PetUpdated91092 | edge_replay:select://select[@id='specialties']:bootstrapspecialty91092");
			safeSelect("//SELECT[@id = 'specialties']", "BootstrapSpecialty91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save Vet | commit:save vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Edit Vet | crud_or_detail:edit vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Edit Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Edit Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK |  | click:div:/html[1]/body[1]/app-root[1]/app-vet-edit[1]/div[1]/div[1]/form[1]/div[4]/div[1]/mat-form-field[1]/div[1]/div[1]/div[1]/mat-select[1]/div[1]/div[1]");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/APP-VET-EDIT[1]/DIV[1]/DIV[1]/FORM[1]/DIV[4]/DIV[1]/MAT-FORM-FIELD[1]/DIV[1]/DIV[1]/DIV[1]/MAT-SELECT[1]/DIV[1]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/APP-VET-EDIT[1]/DIV[1]/DIV[1]/FORM[1]/DIV[4]/DIV[1]/MAT-FORM-FIELD[1]/DIV[1]/DIV[1]/DIV[1]/MAT-SELECT[1]/DIV[1]/DIV[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test030() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | First Name | edge_replay:input://input[@id='firstname']:rlmownerupdated");
			safeType("//INPUT[@id = 'firstName']", "RLMOwnerUpdated");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'firstName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: INPUT | Last Name | edge_replay:input://input[@id='lastname']:updated91092");
			safeType("//INPUT[@id = 'lastName']", "Updated91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //INPUT[@id = 'lastName'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: SELECT | PetUpdated91092 PetUpdated91092 | edge_replay:select://select[@id='specialties']:bootstrapspecialty91092");
			safeSelect("//SELECT[@id = 'specialties']", "BootstrapSpecialty91092");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SELECT[@id = 'specialties'] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Save Vet | commit:save vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Save Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Save Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Delete Vet | crud_or_detail:delete vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Delete Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Delete Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test031() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Veterinarians | crud_or_detail:veterinarians");
			safeClick("//A[contains(normalize-space(.), 'Veterinarians')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Veterinarians')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[1]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add Vet | add_or_open:add vet");
			safeClick("//BUTTON[contains(normalize-space(.), 'Add Vet')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Add Vet')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test032() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the Owners section to add a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | crud_or_detail:owners");
			safeClick("//A[contains(normalize-space(.), 'Owners')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Owners')] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1] | " + e); markDriverBroken(e); }
		try {
			System.out.println("STEP: CLICK | Add New | add_or_open:add new");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1] | " + e); markDriverBroken(e); }
	}

	@Test()
	public void test033() throws Exception {
		System.out.println("SCENARIO_REASON: Exploring the navigation options to find a meaningful action for the next test.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | All | click:a:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1] | " + e); markDriverBroken(e); }
	}

	@AfterClass
	public static void tearDown() throws Exception {
		RuntimeException coverageFailure = null;

		try {
			if (jsCoverage != null) {
				jsCoverage.closeAndWrite();
			}
		} catch (RuntimeException e) {
			coverageFailure = e;
		}

		if (driver != null) {
			if (driverBroken) {
				System.out.println(
					"SELENIUM4_DRIVER_QUIT_SKIPPED | reason=DRIVER_UNRESPONSIVE"
				);
			} else {
				try {
					driver.quit();
				} catch (Exception e) {
					System.out.println(
						"SELENIUM4_DRIVER_QUIT_WARNING | " + e
					);
				}
			}
		}

		if (coverageFailure != null) {
			throw coverageFailure;
		}
	}
}
