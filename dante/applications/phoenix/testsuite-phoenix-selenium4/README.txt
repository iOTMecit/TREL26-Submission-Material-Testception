PETCLINIC SELENIUM 4 EXECUTION PROJECT

This directory is intentionally separate from:
  dante/applications/petclinic/testsuite-petclinic

The original directory remains the raw RLM output. This project is the
Selenium 4 execution copy.

First smoke test:
  mvn -Dtest=smoke.CdpSmokeTest test

Headless smoke test:
  TESTCEPTION_HEADLESS=true mvn -Dtest=smoke.CdpSmokeTest test

Do not run the 26 generated tests before the smoke test passes.

Refresh after a new RLM generation:
  python3 scripts/convert_generated_suite.py     ../testsuite-petclinic/src/main/java/tests/GeneratedTestSuiteFired.java     .

Important:
- GeneratedTestSuiteFiredCoverage.java is not used. It is cdp4j based.
- The converted suite currently preserves STEP_SKIPPED exception swallowing.
  Before final coverage measurement, generator semantics must be changed so
  mandatory failed actions fail the JUnit test.
