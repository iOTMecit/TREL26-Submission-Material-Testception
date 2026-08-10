package tests;

import java.util.concurrent.TimeUnit;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Keys;
import utils.DriverProvider;
import utils.Properties;
import utils.BasePageObject;

public class GeneratedTestSuiteFired {

	private static WebDriver driver;
	private static BasePageObject basePageObject;

	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		driver = DriverProvider.getInstance().getDriver();
		driver.get(Properties.app_url);
		basePageObject = new BasePageObject(driver);
	}

	@Before
	public void setUp() throws Exception {
		driver.get(Properties.app_url);
		Thread.sleep(2500);
	}

	@Test()
	public void test00() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[6]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_login")).clear();
		driver.findElement(By.id("input_login")).sendKeys("foo@bar.com");
		driver.findElement(By.id("input_email")).clear();
		driver.findElement(By.id("input_email")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.sendKeys("foobar123");
		driver.findElement(By.id("input_login")).clear();
		driver.findElement(By.id("input_login")).sendKeys("foo@bar.com");
		driver.findElement(By.id("input_email")).clear();
		driver.findElement(By.id("input_email")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.sendKeys("foobar123");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[4]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_username")).clear();
		driver.findElement(By.id("input_username")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]"))
				.sendKeys("adminadmin");
		driver.findElement(By.id("input_username")).clear();
		driver.findElement(By.id("input_username")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]"))
				.sendKeys("adminadmin");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[3]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_login")).clear();
		driver.findElement(By.id("input_login")).sendKeys("foo@bar.com");
		driver.findElement(By.id("input_email")).clear();
		driver.findElement(By.id("input_email")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.sendKeys("foobar123");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/BUTTON[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[7]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_username")).clear();
		driver.findElement(By.id("input_username")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[2]/INPUT[1]"))
				.sendKeys("adminadmin");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[3]/A[2]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_email")).clear();
		driver.findElement(By.id("input_email")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[4]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_login")).clear();
		driver.findElement(By.id("input_login")).sendKeys("foo@bar.com");
		driver.findElement(By.id("input_email")).clear();
		driver.findElement(By.id("input_email")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.sendKeys("foobar123");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[5]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[2]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[2]/UL[1]/LI[3]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/UL[1]/LI[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/UL[1]/LI[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[4]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/UL[1]/LI[2]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
	}

	@Test()
	public void test01() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/UL[1]/LI[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/UL[1]/LI[2]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[2]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[3]/A[1]"))
				.click();
	}

	@Test()
	public void test02() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_total")).clear();
		driver.findElement(By.id("input_total")).sendKeys("500");
		driver.findElement(By.id("input_total")).clear();
		driver.findElement(By.id("input_total")).sendKeys("500");
		driver.findElement(By.id("input_total")).clear();
		driver.findElement(By.id("input_total")).sendKeys("500");
		driver.findElement(By.id("input_total")).clear();
		driver.findElement(By.id("input_total")).sendKeys("500");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/A[1]"))
				.click();
	}

	@Test()
	public void test03() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[2]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]/DIV[2]/BUTTON[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]/INPUT[2]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]/DIV[2]/BUTTON[2]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[4]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]/DIV[2]/BUTTON[3]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_email")).clear();
		driver.findElement(By.id("input_email")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/FORM[1]/FIELDSET[1]/DIV[2]/DIV[1]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_email")).clear();
		driver.findElement(By.id("input_email")).sendKeys("foo@bar.com");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/FORM[1]/FIELDSET[1]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[4]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]/INPUT[1]"))
				.click();
	}

	@Test()
	public void test04() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/UL[1]/LI[2]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[3]/A[1]"))
				.click();
	}

	@Test()
	public void test05() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/A[1]"))
				.click();
	}

	@Test()
	public void test06() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(By.id("input_total")).clear();
		driver.findElement(By.id("input_total")).sendKeys("500");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/BUTTON[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("input_amount")).clear();
		driver.findElement(By.id("input_amount")).sendKeys("10");
		driver.findElement(By.id("input_description")).clear();
		driver.findElement(By.id("input_description")).sendKeys("description");
		driver.findElement(By.id("input_amount")).clear();
		driver.findElement(By.id("input_amount")).sendKeys("10");
		driver.findElement(By.id("input_description")).clear();
		driver.findElement(By.id("input_description")).sendKeys("description");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[4]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[12]/BUTTON[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[1]/BUTTON[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[14]/BUTTON[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[3]/BUTTON[1]"))
				.click();
	}

	@Test()
	public void test07() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[3]/A[1]"))
				.click();
	}

	@Test()
	public void test08() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[2]/DIV[2]/DIV[2]/UL[1]/LI[2]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]/DIV[2]/BUTTON[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
	}

	@Test()
	public void test09() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[4]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.clear();
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[1]/INPUT[1]"))
				.sendKeys("Personal");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
		driver.findElement(By.id("input_currency")).sendKeys(
				"United States Dollar");
	}

	@Test()
	public void test10() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/A[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(By.id("input_total")).clear();
		driver.findElement(By.id("input_total")).sendKeys("500");
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/FIELDSET[1]/DIV[3]/INPUT[1]"))
				.click();
		Thread.sleep(1500);
		driver.findElement(By.id("add_transaction_text")).clear();
		driver.findElement(By.id("add_transaction_text")).sendKeys(
				"transaction");
		driver.findElement(By.id("add_transaction_text")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("add_transaction_amount")).clear();
		driver.findElement(By.id("add_transaction_amount")).sendKeys("20");
		driver.findElement(By.id("add_transaction_amount"))
				.sendKeys(Keys.ENTER);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[2]/DIV[1]/DIV[1]/DIV[2]/DIV[1]/DIV[2]/DIV[1]/DIV[5]/BUTTON[1]"))
				.click();
	}

	@Test()
	public void test11() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]/DIV[2]/BUTTON[1]"))
				.click();
	}

	@Test()
	public void test12() throws Exception {
		driver.get("http://localhost:3000");
		Thread.sleep(2500);
		driver.findElement(
				By.xpath("/HTML[1]/BODY/DIV[1]/DIV[2]/DIV[3]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/A[1]/DIV[2]/BUTTON[2]"))
				.click();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		driver.quit();
	}

}