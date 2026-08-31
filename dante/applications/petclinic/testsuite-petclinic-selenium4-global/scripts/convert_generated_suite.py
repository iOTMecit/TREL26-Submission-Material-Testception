#!/usr/bin/env python3
"""Convert the latest raw RLM suite into the Selenium 4 execution project."""

from pathlib import Path
import sys

if len(sys.argv) != 3:
    raise SystemExit(
        "Usage: convert_generated_suite.py "
        "<GeneratedTestSuiteFired.java> "
        "<output-project-dir>"
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

text = source.read_text(encoding="utf-8")

text = text.replace("import utils.DriverProvider;\n", "")
text = text.replace("import utils.Properties;\n", "")
text = text.replace("import utils.BasePageObject;\n", "")

text = text.replace(
    "import org.openqa.selenium.support.ui.Select;\n",
    "import org.openqa.selenium.support.ui.Select;\n"
    "import org.openqa.selenium.chrome.ChromeDriver;\n"
    "import org.openqa.selenium.chrome.ChromeOptions;\n"
)

text = text.replace(
    "public class GeneratedTestSuiteFired {",
    "public class GeneratedTestSuiteFiredTest {",
    1,
)

text = text.replace(
    "\tprivate static BasePageObject basePageObject;\n",
    "",
)

text = text.replace(
    "\t\tdriver = DriverProvider.getInstance().getDriver();\n",
    """\t\tChromeOptions options = new ChromeOptions();
\t\tboolean headless = Boolean.parseBoolean(
\t\t\tSystem.getenv().getOrDefault("TESTCEPTION_HEADLESS", "false")
\t\t);
\t\tif (headless) {
\t\t\toptions.addArguments("--headless=new");
\t\t}
\t\toptions.addArguments("--remote-allow-origins=*");
\t\tdriver = new ChromeDriver(options);
""",
    1,
)

text = text.replace(
    "\t\tbasePageObject = new BasePageObject(driver);\n",
    "",
)

output.parent.mkdir(parents=True, exist_ok=True)
output.write_text(text, encoding="utf-8")
print(output)
