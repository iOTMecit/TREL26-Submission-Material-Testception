#!/usr/bin/env python3
"""Convert an RLM-generated legacy DANTE suite into the Selenium4 coverage suite.

The transformation keeps the generated test semantics (STEP_SKIPPED => continue)
while replacing the legacy DriverProvider/session setup with Selenium4 ChromeDriver
and attaching the existing Selenium4JsCoverage collector to the same driver instance.
"""

from pathlib import Path
import re
import sys


if len(sys.argv) != 3:
    raise SystemExit(
        "Usage: prepare-selenium4-suite.py "
        "<GeneratedTestSuiteFired-or-Checked.java> <selenium4-project-root>"
    )

source = Path(sys.argv[1]).resolve()
project = Path(sys.argv[2]).resolve()
output = (
    project
    / "src"
    / "test"
    / "java"
    / "tests"
    / "GeneratedTestSuiteFiredTest.java"
)

if not source.is_file():
    raise SystemExit(f"Raw generated suite not found: {source}")

coverage_class = (
    project
    / "src"
    / "test"
    / "java"
    / "coverage"
    / "Selenium4JsCoverage.java"
)

if not coverage_class.is_file():
    raise SystemExit(
        "Selenium4JsCoverage.java not found in Selenium4 project: "
        f"{coverage_class}"
    )

text = source.read_text(encoding="utf-8")

# ------------------------------------------------------------
# Legacy generated suites use utils.Properties.app_url.
# The Selenium4 harness must be application-agnostic, so replace those
# references before removing the legacy utils.Properties import.
# TESTCEPTION_APP_URL is supplied by the application-aware runners.
# TESTCEPTION_WAIT_URL is a safe fallback for older wrappers.
# ------------------------------------------------------------

properties_app_url_updates = text.count("Properties.app_url")
text = text.replace("Properties.app_url", "APP_URL")

# ------------------------------------------------------------
# Remove legacy DANTE execution-only imports.
# ------------------------------------------------------------

for legacy_import in (
    "import utils.DriverProvider;\n",
    "import utils.Properties;\n",
    "import utils.BasePageObject;\n",
):
    text = text.replace(legacy_import, "")

# ------------------------------------------------------------
# Selenium4 + JUnit coverage imports.
# ------------------------------------------------------------

anchor = "import org.openqa.selenium.support.ui.Select;\n"

selenium4_imports = """import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.HasCdp;
import org.junit.Rule;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import coverage.Selenium4JsCoverage;
"""

if anchor not in text:
    raise SystemExit(
        "Expected Selenium Select import was not found in generated suite."
    )

text = text.replace(anchor, selenium4_imports, 1)

# ------------------------------------------------------------
# Rename class for Maven/Selenium4 project.
# ------------------------------------------------------------

source_class_match = re.search(
    r"public class (GeneratedTestSuite(?:Fired|Checked)) \{",
    text,
)
if source_class_match is None:
    raise SystemExit(
        "Expected GeneratedTestSuiteFired or GeneratedTestSuiteChecked "
        "class declaration was not found."
    )

source_class = source_class_match.group(1)
text = text.replace(
    f"public class {source_class} {{",
    "public class GeneratedTestSuiteFiredTest {",
    1,
)

# ------------------------------------------------------------
# Remove legacy BasePageObject state.
# ------------------------------------------------------------

text = text.replace(
    "\tprivate static BasePageObject basePageObject;\n",
    "",
)

# ------------------------------------------------------------
# Attach coverage lifecycle to the same Selenium4 driver.
# ------------------------------------------------------------

driver_decl = "\tprivate static WebDriver driver;\n"

coverage_watcher = """	private static WebDriver driver;
	private static final String APP_URL =
		System.getenv().getOrDefault(
			"TESTCEPTION_APP_URL",
			System.getenv().getOrDefault(
				"TESTCEPTION_WAIT_URL",
				"http://localhost:3000/"
			)
		);
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

		/*
		 * Do not call manage().window().maximize() after Chrome startup.
		 * Chrome 114 occasionally loses the initial window before that
		 * command is processed and returns "no such window".
		 */
		options.addArguments("--window-size=1920,1080");

		RuntimeException lastError = null;

		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				return new ChromeDriver(options);
			} catch (RuntimeException e) {
				lastError = e;

				System.out.println(
					"SELENIUM4_BROWSER_CREATE_RETRY | attempt="
					+ attempt
					+ "/3 | "
					+ e.getClass().getSimpleName()
					+ " | "
					+ String.valueOf(e.getMessage())
				);

				if (attempt < 3) {
					try {
						Thread.sleep(750L);
					} catch (InterruptedException interrupted) {
						Thread.currentThread().interrupt();
						throw new RuntimeException(
							"Interrupted while retrying ChromeDriver creation",
							interrupted
						);
					}
				}
			}
		}

		throw lastError != null
			? lastError
			: new IllegalStateException(
				"ChromeDriver creation failed"
			);
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
		WebDriver oldDriver = driver;

		if (!oldDriverBroken && jsCoverage != null) {
			try {
				jsCoverage.prepareForBrowserRecycle();
			} catch (RuntimeException e) {
				oldDriverBroken = true;
				driverBroken = true;

				System.out.println(
					"SELENIUM4_RECYCLE_PREPARE_WARNING | "
					+ e.getClass().getSimpleName()
					+ " | "
					+ String.valueOf(e.getMessage())
				);
			}
		}

		/*
		 * Important:
		 * Create the replacement before touching the static driver.
		 * A transient Chrome startup failure must not leave driver=null.
		 */
		WebDriver replacementDriver = createChromeDriver();

		if (!(replacementDriver instanceof HasCdp)) {
			try {
				replacementDriver.quit();
			} catch (Exception ignored) {
			}

			throw new IllegalStateException(
				"Fresh Chrome driver does not implement HasCdp"
			);
		}

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
				"SELENIUM4_OLD_DRIVER_QUIT_SKIPPED | "
				+ "reason=DRIVER_UNRESPONSIVE"
			);
		}

		try {
			jsCoverage.rebindToNewDriver(
				(HasCdp) replacementDriver
			);

			driver = replacementDriver;
			driverBroken = false;

		} catch (RuntimeException e) {
			try {
				replacementDriver.quit();
			} catch (Exception ignored) {
			}

			driverBroken = true;
			throw e;
		}

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
"""

if driver_decl not in text:
    raise SystemExit("Expected WebDriver declaration was not found.")

text = text.replace(driver_decl, coverage_watcher, 1)

# ------------------------------------------------------------
# Replace legacy DriverProvider with Selenium4 ChromeDriver.
# Coverage is started on exactly this same driver via HasCdp.
# ------------------------------------------------------------

old_driver_setup = "\t\tdriver = DriverProvider.getInstance().getDriver();\n"

new_driver_setup = """		driver = createChromeDriver();

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
"""

if old_driver_setup not in text:
    raise SystemExit(
        "Expected legacy DriverProvider setup was not found. "
        "The raw generated suite format may have changed."
    )

text = text.replace(old_driver_setup, new_driver_setup, 1)

text = text.replace(
    "\t\tbasePageObject = new BasePageObject(driver);\n",
    "",
)

# ------------------------------------------------------------
# Preserve RLM semantics: failed individual actions are skipped and
# the JUnit test continues. Add the exception text to the log if the
# generator emitted the historical short STEP_SKIPPED form.
# ------------------------------------------------------------

skip_pattern = re.compile(
    r'System\.out\.println\("STEP_SKIPPED: ([^"\\]*(?:\\.[^"\\]*)*)"\);'
)


def add_exception_to_skip(match: re.Match[str]) -> str:
    locator = match.group(1)
    return (
        f'System.out.println("STEP_SKIPPED: {locator} | " + e); '
        'markDriverBroken(e);'
    )


text, skip_log_updates = skip_pattern.subn(add_exception_to_skip, text)

# Once a ChromeDriver transport timeout is detected, avoid issuing additional
# WebDriver commands from the remaining steps of that same generated test.
helper_guard_pattern = re.compile(
    r'(\tprivate void (?:safeClick|safeSaveButtonClick|safeType|safeSelect)\([^\n]*\) throws Exception \{\n)'
)

def add_driver_guard(match: re.Match[str]) -> str:
    return match.group(1) + "\t\tensureDriverAvailableForStep();\n"

text, helper_guard_updates = helper_guard_pattern.subn(
    add_driver_guard,
    text,
)

# Explicit safety check: the bridge must never turn STEP_SKIPPED into throw e.
if "STEP_FAILED:" in text:
    raise SystemExit(
        "Strict STEP_FAILED/throw semantics detected in transformed suite. "
        "Refusing to overwrite the Selenium4 suite."
    )

# ------------------------------------------------------------
# Replace legacy teardown with coverage finalization + driver quit.
# ------------------------------------------------------------

teardown_pattern = re.compile(
    r'\t@AfterClass\s*\n'
    r'\tpublic static void tearDown\(\) throws Exception \{.*?'
    r'\n\t\}\s*\n\}\s*$',
    re.DOTALL,
)

new_teardown = """\t@AfterClass
\tpublic static void tearDown() throws Exception {
\t\tRuntimeException coverageFailure = null;

\t\ttry {
\t\t\tif (jsCoverage != null) {
\t\t\t\tjsCoverage.closeAndWrite();
\t\t\t}
\t\t} catch (RuntimeException e) {
\t\t\tcoverageFailure = e;
\t\t}

\t\tif (driver != null) {
\t\t\tif (driverBroken) {
\t\t\t\tSystem.out.println(
\t\t\t\t\t"SELENIUM4_DRIVER_QUIT_SKIPPED | reason=DRIVER_UNRESPONSIVE"
\t\t\t\t);
\t\t\t} else {
\t\t\t\ttry {
\t\t\t\t\tdriver.quit();
\t\t\t\t} catch (Exception e) {
\t\t\t\t\tSystem.out.println(
\t\t\t\t\t\t"SELENIUM4_DRIVER_QUIT_WARNING | " + e
\t\t\t\t\t);
\t\t\t\t}
\t\t\t}
\t\t}

\t\tif (coverageFailure != null) {
\t\t\tthrow coverageFailure;
\t\t}
\t}
}
"""

text, teardown_updates = teardown_pattern.subn(new_teardown, text, count=1)

if teardown_updates != 1:
    raise SystemExit(
        "Expected generated @AfterClass block was not found. "
        "The raw generated suite format may have changed."
    )

# ------------------------------------------------------------
# Sanity checks before overwriting the working Selenium4 suite.
# ------------------------------------------------------------

test_count = len(
    re.findall(r"public void test\d{3}\(", text)
)

if test_count == 0:
    raise SystemExit("No generated test methods were found after transformation.")

for forbidden in (
    "DriverProvider.getInstance()",
    "BasePageObject basePageObject",
    "public class GeneratedTestSuiteFired {",
    "public class GeneratedTestSuiteChecked {",
):
    if forbidden in text:
        raise SystemExit(
            f"Legacy Selenium3/DANTE token still present after transformation: {forbidden}"
        )

# TESTCEPTION_REMOVE_LEGACY_MAXIMIZE_V1
# Legacy DANTE suites may contain a second maximize() in setUp().
# The Selenium4 bridge already gives Chrome a deterministic window size
# through ChromeOptions, so remove any post-start maximize command.
text = re.sub(
    r"(?m)^[ \t]*driver\.manage\(\)\.window\(\)\.maximize\(\);[ \t]*\n?",
    "",
    text,
)

output.parent.mkdir(parents=True, exist_ok=True)
output.write_text(text, encoding="utf-8")

# TESTCEPTION_PER_TEST_BACKEND_RESET_BRIDGE_V1
# Keep application-specific backend isolation outside the core Selenium4
# transformation so fresh-browser and coverage logic remain unchanged.
postprocessor = (
    Path(__file__).resolve().with_name(
        "inject-per-test-reset.py"
    )
)
if postprocessor.is_file():
    import subprocess
    subprocess.run(
        [
            sys.executable,
            str(postprocessor),
            str(output),
        ],
        check=True,
    )


print(f"Selenium4 suite prepared: {output}")
print(f"Source suite strategy    : {source_class}")
print(f"Properties.app_url fixed : {properties_app_url_updates}")
print(f"Generated tests          : {test_count}")
print(f"STEP_SKIPPED logs updated: {skip_log_updates}")
print(f"Driver guards inserted   : {helper_guard_updates}")
print("Browser recycle default : every test (1)")
print("Step exception policy    : SKIP AND CONTINUE")
