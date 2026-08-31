package tests;

import coverage.Selenium4JsCoverage;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.HasCdp;

/**
 * Zero-user-action startup coverage baseline.
 *
 * Purpose:
 *   Measure how much JavaScript/application coverage is obtained simply by
 *   loading the Petclinic application, before any generated user interaction.
 *
 * Exact DevTools-parity lifecycle:
 *
 *   1. ChromeDriver is created while still on its initial blank/data page.
 *   2. Selenium4JsCoverage is started BEFORE the application is visited.
 *   3. TestWatcher.beginTest() starts the measured test coverage lifecycle.
 *   4. test000 visits the application exactly ONCE.
 *   5. No clicks, typing, selection, form submission, or extra navigation.
 *
 * There is deliberately NO warm application load and NO second reload.
 */
public class StartupOnlyCoverageTest {

    private static final String APP_URL =
            System.getenv().getOrDefault(
                    "TESTCEPTION_STARTUP_URL",
                    "http://localhost:3000/");

    private static final long WAIT_MS =
            Long.parseLong(
                    System.getenv().getOrDefault(
                            "TESTCEPTION_STARTUP_WAIT_MS",
                            "250"));

    private static WebDriver driver;
    private static Selenium4JsCoverage jsCoverage;

    @Rule
    public final TestWatcher coverageWatcher = new TestWatcher() {

        private String name(Description description) {
            return description.getClassName()
                    + "#"
                    + description.getMethodName();
        }

        @Override
        protected void starting(Description description) {
            jsCoverage.beginTest(name(description));
        }

        @Override
        protected void succeeded(Description description) {
            jsCoverage.endTest(name(description), true);
        }

        @Override
        protected void failed(
                Throwable error,
                Description description) {

            try {
                jsCoverage.endTest(name(description), false);
            } catch (RuntimeException coverageError) {
                error.addSuppressed(coverageError);
            }
        }

        @Override
        protected void skipped(
                AssumptionViolatedException error,
                Description description) {

            jsCoverage.abortTest(
                    name(description),
                    "SKIPPED");
        }

        @Override
        protected void finished(Description description) {
            jsCoverage.abortTest(
                    name(description),
                    "ABORTED");
        }
    };

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {

        ChromeOptions options = new ChromeOptions();

        boolean headless = Boolean.parseBoolean(
                System.getenv().getOrDefault(
                        "TESTCEPTION_HEADLESS",
                        "false"));

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);

        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {
            // Keep behavior non-fatal in headless/minimal display environments.
        }

        if (!(driver instanceof HasCdp)) {
            throw new IllegalStateException(
                    "Active Chrome driver does not implement HasCdp");
        }

        jsCoverage = Selenium4JsCoverage.fromEnvironment(
                (HasCdp) driver);

        jsCoverage.start();

        System.out.println(
                "DEVTOOLS_PARITY_COVERAGE_STARTED_BEFORE_APP_LOAD"
                        + " | currentUrl=" + driver.getCurrentUrl());
    }

    @Test
    public void test000() throws Exception {

        System.out.println(
                "SCENARIO_REASON: Zero-user-action startup baseline.");

        System.out.println(
                "DEVTOOLS_PARITY_SINGLE_MEASURED_LOAD"
                        + " | url=" + APP_URL);

        // Exactly one application load after coverage has started.
        driver.get(APP_URL);
        Thread.sleep(WAIT_MS);

        // Deliberately no user interaction after application load.
        System.out.println(
                "STARTUP_BASELINE_USER_ACTIONS=0");
    }

    @AfterClass
    public static void tearDown() throws Exception {

        RuntimeException coverageFailure = null;

        try {
            if (jsCoverage != null) {
                jsCoverage.closeAndWrite();
            }
        } catch (RuntimeException error) {
            coverageFailure = error;
        }

        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            if (coverageFailure != null) {
                throw coverageFailure;
            }
        }
    }
}
