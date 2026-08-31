package smoke;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.HasCdp;

public class CdpSmokeTest {

    private static WebDriver driver;

    @BeforeClass
    public static void setUpClass() {
        ChromeOptions options = new ChromeOptions();

        boolean headless = Boolean.parseBoolean(
            System.getenv().getOrDefault(
                "TESTCEPTION_HEADLESS",
                "false"
            )
        );

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @Test
    public void sameDriverCanExecuteCdpCommand() {
        assertTrue(
            "The active Chrome driver must implement HasCdp",
            driver instanceof HasCdp
        );

        HasCdp cdp = (HasCdp) driver;

        Map<String, Object> result =
            cdp.executeCdpCommand(
                "Browser.getVersion",
                Collections.emptyMap()
            );

        assertNotNull(result);
        assertNotNull(result.get("product"));

        System.out.println("CDP Browser.getVersion: " + result);
    }

    @AfterClass
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }
}
