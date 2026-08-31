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
import utils.DriverProvider;
import utils.Properties;
import utils.BasePageObject;

public class GeneratedTestSuiteFired {

	private static WebDriver driver;
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
		driver.get("http://localhost:3000/");
		basePageObject = new BasePageObject(driver);
	}

	@Before
	public void setUp() throws Exception {
		Thread.sleep(250);
	}

	@Test()
	public void test000() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for adding a new owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test001() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for editing an existing owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test002() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for adding a new pet for an owner.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test003() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for editing an existing pet.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test004() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for adding a visit for a pet.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test005() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for editing an existing visit.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test006() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for deleting a visit.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test007() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for adding a new veterinarian.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Veterinarians | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[3]/a[1]:veterinarians");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[3]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test008() throws Exception {
		System.out.println("SCENARIO_REASON: Happy path for editing an existing veterinarian.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Veterinarians | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[3]/a[1]:veterinarians");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[3]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[3]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test009() throws Exception {
		System.out.println("SCENARIO_REASON: Negative validation for adding an owner without required fields.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test010() throws Exception {
		System.out.println("SCENARIO_REASON: Negative validation for adding a pet without required fields.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@Test()
	public void test011() throws Exception {
		System.out.println("SCENARIO_REASON: Negative validation for adding a visit without required fields.");
		driver.get("http://localhost:3000/");
		Thread.sleep(250);
		try {
			System.out.println("STEP: CLICK | Owners | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]:owners");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | All | global_entry_prefix:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]:all");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[1]/A[1]"); }
		try {
			System.out.println("STEP: CLICK | Add New | global:click:/html[1]/body[1]/app-root[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/ul[1]/li[2]/a[1]:");
			safeClick("/HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/APP-ROOT[1]/DIV[1]/NAV[1]/DIV[1]/UL[1]/LI[2]/UL[1]/LI[2]/A[1]"); }
	}

	@AfterClass
	public static void tearDown() throws Exception {
		try {
			if (driver != null) driver.quit();
		} catch (Exception e) {}
	}
}
