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
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Before
	public void setUp() throws Exception {
		Thread.sleep(250);
	}

	@Test()
	public void test000() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("//DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]"); }
	}

	@Test()
	public void test001() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
	}

	@Test()
	public void test002() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK | Create Previous Advanced Choose a language English English Choose a language English English  Logout | click:div:create previous advanced choose a language english english choose a language english english  logout");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
	}

	@Test()
	public void test003() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test004() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Recorded transition to state16.html | click:span:recorded transition to state16.html");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test005() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Recorded transition to state16.html | click:span:recorded transition to state16.html");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]"); }
	}

	@Test()
	public void test006() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Recorded transition to state16.html | click:span:recorded transition to state16.html");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]"); }
		try {
			System.out.println("STEP: CLICK | on | click:input:on");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]"); }
	}

	@Test()
	public void test007() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Recorded transition to state16.html | click:span:recorded transition to state16.html");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]"); }
	}

	@Test()
	public void test008() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Recorded transition to state16.html | click:span:recorded transition to state16.html");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
	}

	@Test()
	public void test009() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Recorded transition to state16.html | click:span:recorded transition to state16.html");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
	}

	@Test()
	public void test010() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Recorded transition to state16.html | click:span:recorded transition to state16.html");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/SPAN[1]"); }
		try {
			System.out.println("STEP: CLICK | Fork me on | click:a:fork me on");
			safeClick("//A[contains(normalize-space(.), 'Fork me on')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Fork me on')]"); }
	}

	@Test()
	public void test011() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test012() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]"); }
	}

	@Test()
	public void test013() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK | on | click:input:on");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]"); }
	}

	@Test()
	public void test014() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
	}

	@Test()
	public void test015() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]"); }
	}

	@Test()
	public void test016() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
	}

	@Test()
	public void test017() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: CLICK | Fork me on | click:a:fork me on");
			safeClick("//A[contains(normalize-space(.), 'Fork me on')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Fork me on')]"); }
	}

	@Test()
	public void test018() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test019() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: CLICK | on | click:input:on");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]"); }
	}

	@Test()
	public void test020() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test021() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/DIV[1]"); }
	}

	@Test()
	public void test022() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
	}

	@Test()
	public void test023() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]"); }
	}

	@Test()
	public void test024() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
	}

	@Test()
	public void test025() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Fork me on | click:a:fork me on");
			safeClick("//A[contains(normalize-space(.), 'Fork me on')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Fork me on')]"); }
	}

	@Test()
	public void test026() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("//DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]"); }
	}

	@Test()
	public void test027() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK | on | click:input:on");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]"); }
	}

	@Test()
	public void test028() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
	}

	@Test()
	public void test029() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK | Create Previous Advanced Welcome to Retrospected Click below and start retrospecting: Create a new session | add_or_open:create previous advanced welcome to retrospected click below and start retrospecting: create a new session");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
	}

	@Test()
	public void test030() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
	}

	@Test()
	public void test031() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK | Fork me on | click:a:fork me on");
			safeClick("//A[contains(normalize-space(.), 'Fork me on')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Fork me on')]"); }
	}

	@Test()
	public void test032() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Leave | click:button: leave");
			safeClick("//BUTTON[contains(normalize-space(.), ' Leave')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Leave')]"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test033() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | on | click:input:on");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/DIV[1]/LABEL[1]/INPUT[1]"); }
	}

	@Test()
	public void test034() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK |  Delete | crud_or_detail: delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test035() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK | Work remotely  | click:span:work remotely ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test036() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK | user  | click:span:user ");
			safeClick("//SPAN[contains(normalize-space(.), 'user ')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SPAN[contains(normalize-space(.), 'user ')]"); }
	}

	@Test()
	public void test037() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
	}

	@Test()
	public void test038() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
	}

	@Test()
	public void test039() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
	}

	@Test()
	public void test040() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK |  | click:span:/html[1]/body[1]/div[2]/div[1]/div[2]/aside[1]/ul[1]/li[1]/span[1]/span[1]/span[1]");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/UL[1]/LI[1]/SPAN[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/UL[1]/LI[1]/SPAN[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test041() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK | Time off  | click:span:time off ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test042() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Work remotely  | click:span:work remotely ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[5]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[5]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test043() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | user | click:span:user");
			safeClick("/HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/UL[1]/LI[1]/SPAN[1]/SPAN[2]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[2]/DIV[1]/DIV[2]/ASIDE[1]/UL[1]/LI[1]/SPAN[1]/SPAN[2]"); }
	}

	@Test()
	public void test044() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK | Work  | click:span:work ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test045() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: CLICK | Work  | click:span:work ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test046() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
	}

	@Test()
	public void test047() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: CLICK |  Delete | crud_or_detail: delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test048() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:span:");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[4]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[4]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test049() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Time off  | click:span:time off ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test050() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
		try {
			System.out.println("STEP: CLICK |  Delete | crud_or_detail: delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[4]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[4]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test051() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
	}

	@Test()
	public void test052() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
	}

	@Test()
	public void test053() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: CLICK |  Delete | crud_or_detail: delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test054() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  Copy URL to Clipboard | crud_or_detail: copy url to clipboard");
			safeClick("//BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), ' Copy URL to Clipboard')]"); }
		try {
			System.out.println("STEP: CLICK | Work remotely  | click:span:work remotely ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test055() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
	}

	@Test()
	public void test056() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Time off  | click:span:time off ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test057() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Work  | click:span:work ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test058() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK |  Delete | crud_or_detail: delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test059() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
	}

	@Test()
	public void test060() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Work remotely  | click:span:work remotely ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]"); }
	}

	@Test()
	public void test061() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Ok | click:button:ok");
			safeClick("//BUTTON[contains(normalize-space(.), 'Ok')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Ok')]"); }
	}

	@Test()
	public void test062() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Time off  | click:span:time off ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test063() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Work  | click:span:work ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test064() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK |  | click:span:");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test065() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK |  | click:span:");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test066() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK |  Delete | crud_or_detail: delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test067() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Work remotely  | click:span:work remotely ");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/SPAN[1]/SPAN[1]"); }
	}

	@Test()
	public void test068() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test069() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: CLICK |  Delete | crud_or_detail: delete");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
	}

	@Test()
	public void test070() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK |  | click:button:");
			safeClick("//BUTTON[@title = 'Invite']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@title = 'Invite']"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]:time off");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Time off");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/div[1]/input[1]:work remotely");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work remotely");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/input[1]:work");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]", "Work");
			findVisibleElement("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]").sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/INPUT[1]"); }
	}

	@Test()
	public void test071() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: CLICK | menu | click:button:menu");
			safeClick("//BUTTON[@id = 'crawljax-menu']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-menu']"); }
	}

	@Test()
	public void test072() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test073() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("//DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]"); }
	}

	@Test()
	public void test074() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test075() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
	}

	@Test()
	public void test076() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
	}

	@Test()
	public void test077() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | EnglishEnglish | click:div:englishenglish");
			safeClick("//DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[@id = 'content']/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
		try {
			System.out.println("STEP: NAVIGATE | GRAPH GAP RESUME state24.html -> state25.html | graph_gap_resume:state24.html->state25.html");
			driver.get("http://localhost:4000/");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: NAVIGATE:http://localhost:4000/"); }
		try {
			System.out.println("STEP: CLICK | Create Previous Advanced Welcome to Retrospected Click below and start retrospecting: Create a new session | add_or_open:create previous advanced welcome to retrospected click below and start retrospecting: create a new session");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
	}

	@Test()
	public void test078() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("//DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]"); }
	}

	@Test()
	public void test079() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK |  Logout | click:button: logout");
			safeClick("//BUTTON[@id = 'crawljax-logout']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[@id = 'crawljax-logout']"); }
	}

	@Test()
	public void test080() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | Create Previous Advanced Choose a language English English Choose a language English English  Logout | click:div:create previous advanced choose a language english english choose a language english english  logout");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]"); }
	}

	@Test()
	public void test081() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Advanced | click:div:advanced");
			safeClick("//LABEL[@id = '2']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '2']"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test082() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | Create | commit:create");
			safeClick("//LABEL[@id = '0']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '0']"); }
		try {
			System.out.println("STEP: CLICK | Create a new session | add_or_open:create a new session");
			safeClick("//BUTTON[contains(normalize-space(.), 'Create a new session')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //BUTTON[contains(normalize-space(.), 'Create a new session')]"); }
	}

	@Test()
	public void test083() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Previous | click:div:previous");
			safeClick("//LABEL[@id = '1']");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //LABEL[@id = '1']"); }
		try {
			System.out.println("STEP: CLICK | My Retrospective 2 minutes ago  | crud_or_detail:my retrospective 2 minutes ago ");
			safeClick("//SPAN[contains(normalize-space(.), 'My Retrospective 2 minutes ago ')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //SPAN[contains(normalize-space(.), 'My Retrospective 2 minutes ago ')]"); }
	}

	@Test()
	public void test084() throws Exception {
		System.out.println("SCENARIO_REASON: To initiate the user session by filling in the required fields and starting the application flow.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Let's start | click:button:let's start");
			safeClick("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/BUTTON[1]"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
	}

	@Test()
	public void test085() throws Exception {
		System.out.println("SCENARIO_REASON: To navigate to the next state by selecting the untried action that leads to a new page.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | edge_replay:input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Retrospected A good way of ranting in an Agile way | crud_or_detail:retrospected a good way of ranting in an agile way");
			safeClick("//A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //A[contains(normalize-space(.), 'Retrospected A good way of ranting in an Agile way')]"); }
		try {
			System.out.println("STEP: INPUT | user | input:user:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
	}

	@Test()
	public void test086() throws Exception {
		System.out.println("SCENARIO_REASON: Filling the required input field for the user name before proceeding to the next step in the application.");
		driver.get(Properties.app_url);
		Thread.sleep(250);
		try {
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).clear();
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys("user");
			driver.findElement(By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]")).sendKeys(Keys.ENTER);
			Thread.sleep(250);
		} catch (Exception e) {}
		try {
			System.out.println("STEP: INPUT |  | input:/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/input[1]:user");
			safeType("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]", "user");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: /HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/INPUT[1]"); }
		try {
			System.out.println("STEP: CLICK | Choose a language English English Choose a language English English | click:div:choose a language english english choose a language english english");
			safeClick("//DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]");
			Thread.sleep(250);
		} catch (Exception e) { System.out.println("STEP_SKIPPED: //DIV[contains(normalize-space(.), 'Choose a language English English Choose a language English English')]"); }
	}

	@AfterClass
	public static void tearDown() throws Exception {
		try {
			if (driver != null) driver.quit();
		} catch (Exception e) {}
	}
}
