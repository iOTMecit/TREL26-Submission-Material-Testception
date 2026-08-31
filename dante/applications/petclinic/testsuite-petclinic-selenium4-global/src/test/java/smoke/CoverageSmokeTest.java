package smoke;

import static org.junit.Assert.assertTrue;

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

import coverage.Selenium4JsCoverage;

public class CoverageSmokeTest {

    private static WebDriver driver;
    private static Selenium4JsCoverage jsCoverage;

    @Rule
    public final TestWatcher coverageWatcher =
            new TestWatcher() {

        private String name(
                Description description) {

            return description.getClassName()
                    + "#"
                    + description.getMethodName();
        }

        @Override
        protected void starting(
                Description description) {

            jsCoverage.beginTest(
                    name(description));
        }

        @Override
        protected void succeeded(
                Description description) {

            jsCoverage.endTest(
                    name(description),
                    true);
        }

        @Override
        protected void failed(
                Throwable error,
                Description description) {

            try {
                jsCoverage.endTest(
                        name(description),
                        false);
            } catch (RuntimeException coverageError) {
                error.addSuppressed(
                        coverageError);
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
        protected void finished(
                Description description) {

            jsCoverage.abortTest(
                    name(description),
                    "ABORTED");
        }
    };

    @BeforeClass
    public static void setUpClass() {
        ChromeOptions options =
                new ChromeOptions();

        boolean headless =
                Boolean.parseBoolean(
                        System.getenv().getOrDefault(
                                "TESTCEPTION_HEADLESS",
                                "false"));

        if (headless) {
            options.addArguments(
                    "--headless=new");
        }

        options.addArguments(
                "--remote-allow-origins=*");

        driver =
                new ChromeDriver(options);

        if (!(driver instanceof HasCdp)) {
            throw new IllegalStateException(
                    "Active Chrome driver"
                            + " does not implement HasCdp");
        }

        jsCoverage =
                Selenium4JsCoverage.fromEnvironment(
                        (HasCdp) driver);

        // Coverage starts before application navigation.
        jsCoverage.start();
    }

    @Test
    public void petclinicNavigationProducesCoverage() {
        driver.get(
                "http://localhost:3000/");

        assertTrue(
                "Petclinic navigation did not complete",
                driver.getCurrentUrl().contains(
                        "/petclinic/"));
    }

    @AfterClass
    public static void tearDownClass() {
        RuntimeException coverageFailure =
                null;

        try {
            if (jsCoverage != null) {
                jsCoverage.closeAndWrite();
            }
        } catch (RuntimeException exception) {
            coverageFailure =
                    exception;
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
