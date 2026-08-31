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
 * Important:
 *   @BeforeClass performs one warm application load so the coverage lifecycle
 *   is initialized exactly like the generated Testception suites.
 *
 *   TestWatcher.beginTest() then:
 *     - MODERN_RAW: drains the warm-load counters into the denominator only.
 *     - DANTE: starts precise coverage for the test.
 *
 *   test000 performs exactly one measured application reload and then no
 *   clicks, typing, selection, form submission, or navigation actions.
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

        // Warm load. MODERN_RAW beginTest() drains this from credited coverage.
        // DANTE precise coverage has not started yet.
        driver.get(APP_URL);
        Thread.sleep(WAIT_MS);

        System.out.println(
                "STARTUP_BASELINE_WARM_LOAD_DONE"
                        + " | url=" + APP_URL
                        + " | waitMs=" + WAIT_MS);
    }

    @Test
    public void test000() throws Exception {

        System.out.println(
                "SCENARIO_REASON: Zero-user-action startup baseline.");

        System.out.println(
                "STARTUP_BASELINE_MEASURED_RELOAD"
                        + " | url=" + APP_URL);

        // This reload mirrors the driver.get(start_url) performed at the
        // beginning of every generated Testception test method.
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
