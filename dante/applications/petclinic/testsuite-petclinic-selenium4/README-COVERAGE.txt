PETCLINIC SELENIUM 4 PRECISE JS COVERAGE STAGE

Metric
------
V8 precise JavaScript byte coverage, collected through HasCdp on the same
ChromeDriver used by the generated JUnit test.

Suite rules
-----------
1. Profiler starts before the first application navigation.
2. takePreciseCoverage delimits tests and resets V8 execution counters.
3. Known byte ranges from every observed test form the denominator.
4. Covered ranges are merged only from successful JUnit tests.
5. Nested CDP ranges are resolved by the most-specific containing range.
6. Generated action failures are rethrown; STEP_SKIPPED is not accepted.

Default script filter
---------------------
^http://localhost:3000/.*\.js(?:\?.*)?$

Environment overrides
---------------------
TESTCEPTION_COVERAGE_DIR
TESTCEPTION_APP
TESTCEPTION_COVERAGE_TECHNIQUE
TESTCEPTION_COVERAGE_INCLUDE_REGEX
TESTCEPTION_COVERAGE_EXCLUDE_REGEX
TESTCEPTION_HEADLESS

Reports
-------
$TESTCEPTION_COVERAGE_DIR/petclinic/selenium4/suite-coverage.json
$TESTCEPTION_COVERAGE_DIR/petclinic/selenium4/suite-coverage.csv
$TESTCEPTION_COVERAGE_DIR/petclinic/selenium4/test-results.csv

Refresh after another RLM generation
------------------------------------
python3 scripts/instrument_generated_suite.py \
  ../testsuite-petclinic/src/main/java/tests/GeneratedTestSuiteFired.java \
  .

First execution
---------------
Run only one generated test:

mvn -Dtest='tests.GeneratedTestSuiteFiredTest#test000' test

The Petclinic application must already respond at:
http://localhost:3000/petclinic/welcome

This stage reports bundle-level V8 byte coverage. Source-map conversion to
original source files/lines is a separate next layer.


Coverage collector smoke test
-----------------------------
mvn -Dtest=smoke.CoverageSmokeTest test

Then inspect:
$TESTCEPTION_COVERAGE_DIR/petclinic/selenium4/suite-coverage.json
