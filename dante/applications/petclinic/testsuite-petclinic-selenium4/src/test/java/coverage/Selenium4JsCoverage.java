package coverage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.openqa.selenium.chromium.HasCdp;

/**
 * Selenium 4 same-driver CDP JavaScript coverage collector.
 *
 * Two execution modes are supported because V8 cannot collect detailed block
 * coverage and DANTE's historical function coverage at the same time:
 *
 * MODERN_RAW (default)
 *   callCount=true, detailed=true
 *   Writes the raw all-bundle byte coverage used for diagnostics and the
 *   source-map post-processor input used for modern application coverage.
 *
 * DANTE
 *   callCount=false, detailed=false
 *   Restarts precise coverage for every test and writes the raw function
 *   ranges needed by the DANTE-compatible post-processor.
 *
 * The public API intentionally matches the previously supplied collector so
 * generated JUnit tests do not need to change.
 */
public final class Selenium4JsCoverage {

    private enum Mode {
        MODERN_RAW,
        DANTE;

        private static Mode fromEnvironment() {
            String raw = getenv("TESTCEPTION_COVERAGE_MODE", "modern_raw")
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .replace('-', '_');

            if (raw.equals("dante") || raw.equals("dante_compatible")) {
                return DANTE;
            }

            if (raw.equals("modern")
                    || raw.equals("raw")
                    || raw.equals("modern_raw")
                    || raw.equals("combined")) {
                return MODERN_RAW;
            }

            throw new IllegalArgumentException(
                    "Unknown TESTCEPTION_COVERAGE_MODE: " + raw);
        }

        private String directoryName() {
            return this == DANTE
                    ? "dante-compatible-run"
                    : "modern-raw-run";
        }
    }

    private HasCdp cdp;
    private final Path outputDirectory;
    private final Pattern includePattern;
    private final Pattern excludePattern;
    private final Mode mode;

    private final Map<String, ScriptAggregate> suiteScripts =
            new LinkedHashMap<>();
    private final List<TestResult> testResults =
            new ArrayList<>();
    private final List<TestRawResult> rawTestResults =
            new ArrayList<>();

    private boolean started;
    private boolean closed;
    private boolean preciseCoverageRunning;
    private boolean cdpUsable = true;
    private String activeTestName;

    private Selenium4JsCoverage(
            HasCdp cdp,
            Path outputDirectory,
            Pattern includePattern,
            Pattern excludePattern,
            Mode mode) {

        this.cdp = Objects.requireNonNull(cdp, "cdp");
        this.outputDirectory = Objects.requireNonNull(
                outputDirectory, "outputDirectory");
        this.includePattern = Objects.requireNonNull(
                includePattern, "includePattern");
        this.excludePattern = Objects.requireNonNull(
                excludePattern, "excludePattern");
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    public static Selenium4JsCoverage fromEnvironment(HasCdp cdp) {
        String baseOutput = getenv(
                "TESTCEPTION_COVERAGE_DIR",
                "target/coverage-results");

        String appName = getenv(
                "TESTCEPTION_APP",
                "petclinic");

        String technique = getenv(
                "TESTCEPTION_COVERAGE_TECHNIQUE",
                "selenium4-three-metrics");

        Mode mode = Mode.fromEnvironment();

        Path output = Paths.get(
                baseOutput,
                appName,
                technique,
                mode.directoryName());

        String defaultInclude = mode == Mode.DANTE
                ? "^http://localhost:3000/main\\.js(?:\\?.*)?$"
                : "^http://localhost:3000/.*\\.js(?:\\?.*)?$";

        String includeRegex = getenv(
                "TESTCEPTION_COVERAGE_INCLUDE_REGEX",
                defaultInclude);

        String excludeRegex = getenv(
                "TESTCEPTION_COVERAGE_EXCLUDE_REGEX",
                "(?!)");

        return new Selenium4JsCoverage(
                cdp,
                output,
                Pattern.compile(includeRegex),
                Pattern.compile(excludeRegex),
                mode);
    }

    private static String getenv(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty()
                ? defaultValue
                : value.trim();
    }

    public synchronized void start() {
        ensureNotClosed();

        if (started) {
            return;
        }

        try {
            cdp.executeCdpCommand(
                    "Profiler.enable",
                    Collections.emptyMap());
            cdpUsable = true;
        } catch (RuntimeException exception) {
            markCdpUnusable();
            throw exception;
        }

        if (mode == Mode.MODERN_RAW) {
            startPreciseCoverage();
        }

        started = true;

        System.out.println(
                "SELENIUM4_COVERAGE_STARTED"
                        + " | mode=" + mode
                        + " | include=" + includePattern.pattern()
                        + " | exclude=" + excludePattern.pattern()
                        + " | output=" + outputDirectory);
    }

    private void startPreciseCoverage() {
        ensureCdpUsable();

        if (preciseCoverageRunning) {
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("callCount", mode == Mode.MODERN_RAW);
        parameters.put("detailed", mode == Mode.MODERN_RAW);
        parameters.put("allowTriggeredUpdates", false);

        try {
            cdp.executeCdpCommand(
                    "Profiler.startPreciseCoverage",
                    parameters);
            preciseCoverageRunning = true;
        } catch (RuntimeException exception) {
            markCdpUnusable();
            throw exception;
        }
    }

    private void stopPreciseCoverage() {
        if (!preciseCoverageRunning) {
            return;
        }

        if (!cdpUsable) {
            preciseCoverageRunning = false;
            return;
        }

        try {
            cdp.executeCdpCommand(
                    "Profiler.stopPreciseCoverage",
                    Collections.emptyMap());
        } catch (RuntimeException exception) {
            markCdpUnusable();
            throw exception;
        } finally {
            preciseCoverageRunning = false;
        }
    }

    private void ensureCdpUsable() {
        if (!cdpUsable) {
            throw new IllegalStateException(
                    "Coverage CDP driver is not usable; browser recycle required");
        }
    }

    private void markCdpUnusable() {
        cdpUsable = false;
        preciseCoverageRunning = false;
    }

    /**
     * Called between tests before a planned browser recycle. The accumulated
     * suite/test coverage stays in this collector; only the current CDP
     * transport is detached.
     */
    public synchronized void prepareForBrowserRecycle() {
        ensureStarted();

        if (activeTestName != null) {
            throw new IllegalStateException(
                    "Cannot recycle browser while coverage test is active: "
                            + activeTestName);
        }

        RuntimeException cleanupFailure = null;

        if (cdpUsable) {
            try {
                stopPreciseCoverage();
            } catch (RuntimeException exception) {
                cleanupFailure = exception;
            }

            if (cdpUsable) {
                try {
                    cdp.executeCdpCommand(
                            "Profiler.disable",
                            Collections.emptyMap());
                } catch (RuntimeException exception) {
                    cleanupFailure = combine(cleanupFailure, exception);
                }
            }
        }

        markCdpUnusable();

        if (cleanupFailure != null) {
            System.out.println(
                    "SELENIUM4_COVERAGE_RECYCLE_CLEANUP_WARNING: "
                            + cleanupFailure.getClass().getSimpleName()
                            + " | "
                            + String.valueOf(cleanupFailure.getMessage()));
        }
    }

    /**
     * Bind the same aggregate collector to a fresh ChromeDriver/HasCdp.
     * WebDriver actions and CDP commands for the following tests therefore use
     * exactly the same new Chrome instance.
     */
    public synchronized void rebindToNewDriver(HasCdp newCdp) {
        ensureStarted();

        if (activeTestName != null) {
            throw new IllegalStateException(
                    "Cannot rebind CDP while coverage test is active: "
                            + activeTestName);
        }

        cdp = Objects.requireNonNull(newCdp, "newCdp");
        cdpUsable = true;
        preciseCoverageRunning = false;

        try {
            cdp.executeCdpCommand(
                    "Profiler.enable",
                    Collections.emptyMap());

            if (mode == Mode.MODERN_RAW) {
                startPreciseCoverage();
            }
        } catch (RuntimeException exception) {
            markCdpUnusable();
            throw exception;
        }

        System.out.println(
                "SELENIUM4_COVERAGE_REBOUND_TO_NEW_DRIVER"
                        + " | mode=" + mode);
    }

    /**
     * Recovery path for a WebDriver/Chrome transport failure inside a test.
     * No command is sent to the broken driver. The affected test contributes
     * no coverage sample and the next test can safely bind a fresh browser.
     */
    public synchronized void abandonTestAfterDriverFailure(
            String testName,
            String status) {

        if (!started) {
            return;
        }

        activeTestName = null;
        markCdpUnusable();

        testResults.add(new TestResult(
                testName,
                status,
                0L,
                0L));

        if (mode == Mode.DANTE) {
            rawTestResults.add(new TestRawResult(
                    testName,
                    status,
                    Collections.emptyMap()));
        }

        System.out.println(
                "SELENIUM4_COVERAGE_TEST_ABANDONED: "
                        + testName
                        + " | status=" + status
                        + " | reason=DRIVER_UNUSABLE");
    }

    /**
     * MODERN_RAW drains setup/page-load counters before the test method and
     * keeps those ranges only in the fixed denominator. DANTE starts precise
     * coverage here, matching DANTE's per-test start/stop lifecycle.
     */
    public synchronized void beginTest(String testName) {
        ensureStarted();

        if (activeTestName != null) {
            throw new IllegalStateException(
                    "Coverage test already active: " + activeTestName);
        }

        if (mode == Mode.MODERN_RAW) {
            Snapshot baseline = takeSnapshot();
            mergeKnownRanges(baseline);
        } else {
            startPreciseCoverage();
        }

        activeTestName = Objects.requireNonNull(testName, "testName");

        System.out.println(
                "SELENIUM4_COVERAGE_TEST_START: " + activeTestName);
    }

    public synchronized void endTest(
            String testName,
            boolean successful) {

        ensureStarted();
        ensureActiveTest(testName);

        RuntimeException failure = null;

        try {
            Snapshot snapshot = takeSnapshot();

            if (mode == Mode.DANTE) {
                stopPreciseCoverage();
            }

            mergeKnownRanges(snapshot);
            if (successful) {
                mergeCoveredRanges(snapshot);
            }

            long totalBytes = snapshot.totalKnownBytes();
            long coveredBytes = snapshot.totalCoveredBytes();
            String status = successful ? "PASSED" : "FAILED";

            testResults.add(new TestResult(
                    testName,
                    status,
                    totalBytes,
                    coveredBytes));

            if (mode == Mode.DANTE) {
                rawTestResults.add(new TestRawResult(
                        testName,
                        status,
                        snapshot.rawScripts));
            }

            System.out.printf(
                    Locale.ROOT,
                    "SELENIUM4_COVERAGE_TEST_END: %s"
                            + " | status=%s"
                            + " | covered=%d"
                            + " | total=%d"
                            + " | percent=%.4f%n",
                    testName,
                    status,
                    coveredBytes,
                    totalBytes,
                    percentage(coveredBytes, totalBytes));

        } catch (RuntimeException exception) {
            failure = exception;
            markCdpUnusable();

            System.out.println(
                    "SELENIUM4_COVERAGE_TEST_END_ERROR: "
                            + testName
                            + " | "
                            + exception.getClass().getSimpleName()
                            + " | "
                            + String.valueOf(exception.getMessage()));

        } finally {
            activeTestName = null;
        }

        if (failure != null) {
            throw failure;
        }
    }

    public synchronized void abortTest(
            String testName,
            String status) {

        if (!started || activeTestName == null) {
            return;
        }

        String abortedTestName = activeTestName;
        activeTestName = null;

        if (mode == Mode.DANTE && preciseCoverageRunning && cdpUsable) {
            try {
                stopPreciseCoverage();
            } catch (RuntimeException exception) {
                System.out.println(
                        "SELENIUM4_COVERAGE_ABORT_CLEANUP_WARNING: "
                                + abortedTestName
                                + " | "
                                + exception.getClass().getSimpleName()
                                + " | "
                                + String.valueOf(exception.getMessage()));
            }
        }

        testResults.add(new TestResult(
                abortedTestName,
                status,
                0L,
                0L));

        if (mode == Mode.DANTE) {
            rawTestResults.add(new TestRawResult(
                    abortedTestName,
                    status,
                    Collections.emptyMap()));
        }

        System.out.println(
                "SELENIUM4_COVERAGE_TEST_ABORT: "
                        + abortedTestName
                        + " | status=" + status
                        + " | snapshot=SKIPPED");
    }

    public synchronized void closeAndWrite() {
        if (closed) {
            return;
        }

        RuntimeException failure = null;

        try {
            if (started) {
                if (activeTestName != null) {
                    if (cdpUsable) {
                        abortTest(activeTestName, "ABORTED");
                    } else {
                        abandonTestAfterDriverFailure(
                                activeTestName,
                                "ABORTED_DRIVER_UNUSABLE");
                    }
                }
                writeReports();
            }
        } catch (RuntimeException exception) {
            failure = exception;
        } finally {
            if (cdpUsable) {
                try {
                    stopPreciseCoverage();
                } catch (RuntimeException exception) {
                    failure = combine(failure, exception);
                }

                if (cdpUsable && started) {
                    try {
                        cdp.executeCdpCommand(
                                "Profiler.disable",
                                Collections.emptyMap());
                    } catch (RuntimeException exception) {
                        failure = combine(failure, exception);
                    }
                }
            } else {
                System.out.println(
                        "SELENIUM4_COVERAGE_CLOSE: CDP cleanup skipped "
                                + "because the driver was marked unusable");
            }

            closed = true;
        }

        if (failure != null) {
            throw failure;
        }
    }

    private static RuntimeException combine(
            RuntimeException current,
            RuntimeException next) {

        if (current == null) {
            return next;
        }
        current.addSuppressed(next);
        return current;
    }

    private void ensureActiveTest(String testName) {
        if (activeTestName == null) {
            throw new IllegalStateException("No active coverage test");
        }
        if (!activeTestName.equals(testName)) {
            throw new IllegalStateException(
                    "Coverage test mismatch. active="
                            + activeTestName
                            + " requested="
                            + testName);
        }
    }

    private Snapshot takeSnapshot() {
        if (!preciseCoverageRunning) {
            return new Snapshot();
        }

        ensureCdpUsable();

        Map<String, Object> response;
        try {
            response = cdp.executeCdpCommand(
                    "Profiler.takePreciseCoverage",
                    Collections.emptyMap());
        } catch (RuntimeException exception) {
            markCdpUnusable();
            throw exception;
        }

        Object rawResult = response.get("result");
        if (!(rawResult instanceof List<?>)) {
            throw new IllegalStateException(
                    "Profiler.takePreciseCoverage response has no result list: "
                            + response);
        }

        Snapshot snapshot = new Snapshot();

        for (Object rawScript : (List<?>) rawResult) {
            if (!(rawScript instanceof Map<?, ?>)) {
                continue;
            }

            Map<?, ?> script = (Map<?, ?>) rawScript;
            Object rawUrl = script.get("url");
            String url = rawUrl == null ? "" : String.valueOf(rawUrl);

            if (!acceptUrl(url)) {
                continue;
            }

            List<RawRange> ranges = extractRanges(script);
            snapshot.rawScripts.put(url, ranges);

            EffectiveRanges effective = resolveEffectiveRanges(ranges);
            if (effective.known.isEmpty()) {
                continue;
            }

            ScriptAggregate aggregate = snapshot.scripts.computeIfAbsent(
                    url,
                    ignored -> new ScriptAggregate());

            aggregate.known.addAll(effective.known.intervals);
            aggregate.covered.addAll(effective.covered.intervals);
        }

        return snapshot;
    }

    private boolean acceptUrl(String url) {
        return url != null
                && !url.isEmpty()
                && includePattern.matcher(url).find()
                && !excludePattern.matcher(url).find();
    }

    private static List<RawRange> extractRanges(Map<?, ?> script) {
        Object rawFunctions = script.get("functions");
        if (!(rawFunctions instanceof List<?>)) {
            return Collections.emptyList();
        }

        List<RawRange> ranges = new ArrayList<>();

        for (Object rawFunction : (List<?>) rawFunctions) {
            if (!(rawFunction instanceof Map<?, ?>)) {
                continue;
            }

            Map<?, ?> function = (Map<?, ?>) rawFunction;
            Object rawName = function.get("functionName");
            String functionName = rawName == null
                    ? ""
                    : String.valueOf(rawName);

            Object rawFunctionRanges = function.get("ranges");
            if (!(rawFunctionRanges instanceof List<?>)) {
                continue;
            }

            for (Object rawRange : (List<?>) rawFunctionRanges) {
                if (!(rawRange instanceof Map<?, ?>)) {
                    continue;
                }

                Map<?, ?> range = (Map<?, ?>) rawRange;
                long start = number(range.get("startOffset"));
                long end = number(range.get("endOffset"));
                long count = number(range.get("count"));

                if (end > start) {
                    ranges.add(new RawRange(
                            functionName,
                            start,
                            end,
                            count));
                }
            }
        }

        return ranges;
    }

    /**
     * Modern/raw report segmentation. Each boundary pair is assigned to the
     * shortest containing V8 range, so a nested zero-count block overrides a
     * positive outer function range.
     */
    private static EffectiveRanges resolveEffectiveRanges(
            List<RawRange> ranges) {

        EffectiveRanges result = new EffectiveRanges();
        if (ranges.isEmpty()) {
            return result;
        }

        TreeSet<Long> boundaries = new TreeSet<>();
        for (RawRange range : ranges) {
            boundaries.add(range.start);
            boundaries.add(range.end);
        }

        List<Long> points = new ArrayList<>(boundaries);

        for (int index = 0; index + 1 < points.size(); index++) {
            long segmentStart = points.get(index);
            long segmentEnd = points.get(index + 1);

            if (segmentEnd <= segmentStart) {
                continue;
            }

            RawRange mostSpecific = null;
            for (RawRange candidate : ranges) {
                if (candidate.start <= segmentStart
                        && candidate.end >= segmentEnd) {
                    if (mostSpecific == null
                            || candidate.length() < mostSpecific.length()
                            || (candidate.length() == mostSpecific.length()
                                && candidate.count < mostSpecific.count)) {
                        mostSpecific = candidate;
                    }
                }
            }

            if (mostSpecific == null) {
                continue;
            }

            Interval segment = new Interval(segmentStart, segmentEnd);
            result.known.add(segment);
            if (mostSpecific.count > 0L) {
                result.covered.add(segment);
            }
        }

        return result;
    }

    private static long number(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value == null) {
            return 0L;
        }
        return Long.parseLong(String.valueOf(value));
    }

    private void mergeKnownRanges(Snapshot snapshot) {
        for (Map.Entry<String, ScriptAggregate> entry
                : snapshot.scripts.entrySet()) {
            ScriptAggregate suite = suiteScripts.computeIfAbsent(
                    entry.getKey(),
                    ignored -> new ScriptAggregate());
            suite.known.addAll(entry.getValue().known.intervals);
        }
    }

    private void mergeCoveredRanges(Snapshot snapshot) {
        for (Map.Entry<String, ScriptAggregate> entry
                : snapshot.scripts.entrySet()) {
            ScriptAggregate suite = suiteScripts.computeIfAbsent(
                    entry.getKey(),
                    ignored -> new ScriptAggregate());
            suite.covered.addAll(entry.getValue().covered.intervals);
        }
    }

    private void writeReports() {
        try {
            Files.createDirectories(outputDirectory);

            writeSuiteJson(outputDirectory.resolve("suite-coverage.json"));
            writeSuiteCsv(outputDirectory.resolve("suite-coverage.csv"));
            writeTestCsv(outputDirectory.resolve("test-results.csv"));

            if (mode == Mode.DANTE) {
                writeRawTestRangesJson(
                        outputDirectory.resolve("test-raw-ranges.json"));
            }

            long total = totalSuiteKnownBytes();
            long covered = totalSuiteCoveredBytes();

            System.out.printf(
                    Locale.ROOT,
                    "SELENIUM4_COVERAGE_SUITE:"
                            + " mode=%s"
                            + " | covered=%d"
                            + " | total=%d"
                            + " | percent=%.4f"
                            + " | scripts=%d"
                            + " | output=%s%n",
                    mode,
                    covered,
                    total,
                    percentage(covered, total),
                    suiteScripts.size(),
                    outputDirectory);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not write Selenium 4 coverage reports",
                    exception);
        }
    }

    private void writeSuiteJson(Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardCharsets.UTF_8)) {

            long total = totalSuiteKnownBytes();
            long covered = totalSuiteCoveredBytes();

            writer.write("{\n");
            writer.write("  \"generatedAt\": \""
                    + json(Instant.now().toString()) + "\",\n");
            writer.write("  \"mode\": \"" + mode + "\",\n");
            writer.write("  \"metric\": \"v8_precise_js_byte_coverage\",\n");
            writer.write("  \"successfulTestsOnlyInCoveredUnion\": true,\n");
            writer.write("  \"knownBytes\": " + total + ",\n");
            writer.write("  \"coveredBytes\": " + covered + ",\n");
            writer.write(String.format(
                    Locale.ROOT,
                    "  \"coveragePercent\": %.6f,%n",
                    percentage(covered, total)));
            writer.write("  \"includeRegex\": \""
                    + json(includePattern.pattern()) + "\",\n");
            writer.write("  \"excludeRegex\": \""
                    + json(excludePattern.pattern()) + "\",\n");
            writer.write("  \"scripts\": [\n");

            List<Map.Entry<String, ScriptAggregate>> entries =
                    new ArrayList<>(suiteScripts.entrySet());
            entries.sort(Comparator.comparing(Map.Entry::getKey));

            for (int index = 0; index < entries.size(); index++) {
                Map.Entry<String, ScriptAggregate> entry = entries.get(index);
                ScriptAggregate aggregate = entry.getValue();
                long scriptTotal = aggregate.known.totalLength();
                long scriptCovered = aggregate.covered.totalLength();

                writer.write("    {\n");
                writer.write("      \"url\": \""
                        + json(entry.getKey()) + "\",\n");
                writer.write("      \"knownBytes\": "
                        + scriptTotal + ",\n");
                writer.write("      \"coveredBytes\": "
                        + scriptCovered + ",\n");
                writer.write(String.format(
                        Locale.ROOT,
                        "      \"coveragePercent\": %.6f,%n",
                        percentage(scriptCovered, scriptTotal)));
                writeIntervalsJson(
                        writer,
                        "knownRanges",
                        aggregate.known.intervals,
                        true);
                writeIntervalsJson(
                        writer,
                        "coveredRanges",
                        aggregate.covered.intervals,
                        false);
                writer.write("    }");
                if (index + 1 < entries.size()) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("  ]\n");
            writer.write("}\n");
        }
    }

    private static void writeIntervalsJson(
            BufferedWriter writer,
            String field,
            List<Interval> intervals,
            boolean commaAfter) throws IOException {

        writer.write("      \"" + field + "\": [");
        for (int index = 0; index < intervals.size(); index++) {
            Interval interval = intervals.get(index);
            if (index > 0) {
                writer.write(", ");
            }
            writer.write("{\"startOffset\":" + interval.start
                    + ",\"endOffset\":" + interval.end + "}");
        }
        writer.write("]" + (commaAfter ? "," : "") + "\n");
    }

    private void writeRawTestRangesJson(Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardCharsets.UTF_8)) {

            writer.write("{\n");
            writer.write("  \"generatedAt\": \""
                    + json(Instant.now().toString()) + "\",\n");
            writer.write("  \"mode\": \"DANTE\",\n");
            writer.write("  \"tests\": [\n");

            for (int testIndex = 0;
                    testIndex < rawTestResults.size();
                    testIndex++) {

                TestRawResult test = rawTestResults.get(testIndex);
                writer.write("    {\n");
                writer.write("      \"name\": \""
                        + json(test.name) + "\",\n");
                writer.write("      \"status\": \""
                        + json(test.status) + "\",\n");
                writer.write("      \"scripts\": [\n");

                List<Map.Entry<String, List<RawRange>>> scripts =
                        new ArrayList<>(test.rawScripts.entrySet());
                scripts.sort(Comparator.comparing(Map.Entry::getKey));

                for (int scriptIndex = 0;
                        scriptIndex < scripts.size();
                        scriptIndex++) {

                    Map.Entry<String, List<RawRange>> script =
                            scripts.get(scriptIndex);
                    writer.write("        {\n");
                    writer.write("          \"url\": \""
                            + json(script.getKey()) + "\",\n");
                    writer.write("          \"ranges\": [\n");

                    List<RawRange> ranges = script.getValue();
                    for (int rangeIndex = 0;
                            rangeIndex < ranges.size();
                            rangeIndex++) {

                        RawRange range = ranges.get(rangeIndex);
                        writer.write("            {\"functionName\":\""
                                + json(range.functionName)
                                + "\",\"startOffset\":"
                                + range.start
                                + ",\"endOffset\":"
                                + range.end
                                + ",\"count\":"
                                + range.count
                                + "}");
                        if (rangeIndex + 1 < ranges.size()) {
                            writer.write(",");
                        }
                        writer.write("\n");
                    }

                    writer.write("          ]\n");
                    writer.write("        }");
                    if (scriptIndex + 1 < scripts.size()) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }

                writer.write("      ]\n");
                writer.write("    }");
                if (testIndex + 1 < rawTestResults.size()) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("  ]\n");
            writer.write("}\n");
        }
    }

    private void writeSuiteCsv(Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardCharsets.UTF_8)) {

            writer.write("url,known_bytes,covered_bytes,coverage_percent\n");
            List<Map.Entry<String, ScriptAggregate>> entries =
                    new ArrayList<>(suiteScripts.entrySet());
            entries.sort(Comparator.comparing(Map.Entry::getKey));

            for (Map.Entry<String, ScriptAggregate> entry : entries) {
                long total = entry.getValue().known.totalLength();
                long covered = entry.getValue().covered.totalLength();
                writer.write(csv(entry.getKey())
                        + "," + total
                        + "," + covered
                        + "," + String.format(
                            Locale.ROOT,
                            "%.6f",
                            percentage(covered, total))
                        + "\n");
            }
        }
    }

    private void writeTestCsv(Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardCharsets.UTF_8)) {

            writer.write("test_name,status,known_bytes,"
                    + "executed_bytes_in_delta,delta_percent\n");

            for (TestResult result : testResults) {
                writer.write(csv(result.name)
                        + "," + result.status
                        + "," + result.totalBytes
                        + "," + result.coveredBytes
                        + "," + String.format(
                            Locale.ROOT,
                            "%.6f",
                            percentage(
                                    result.coveredBytes,
                                    result.totalBytes))
                        + "\n");
            }
        }
    }

    private long totalSuiteKnownBytes() {
        long total = 0L;
        for (ScriptAggregate script : suiteScripts.values()) {
            total += script.known.totalLength();
        }
        return total;
    }

    private long totalSuiteCoveredBytes() {
        long total = 0L;
        for (ScriptAggregate script : suiteScripts.values()) {
            total += script.covered.totalLength();
        }
        return total;
    }

    private static double percentage(long covered, long total) {
        return total <= 0L ? 0.0 : covered * 100.0 / total;
    }

    private static String json(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private void ensureStarted() {
        ensureNotClosed();
        if (!started) {
            throw new IllegalStateException(
                    "Coverage has not been started");
        }
    }

    private void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException(
                    "Coverage has already been closed");
        }
    }

    private static final class Snapshot {
        private final Map<String, ScriptAggregate> scripts =
                new LinkedHashMap<>();
        private final Map<String, List<RawRange>> rawScripts =
                new LinkedHashMap<>();

        private long totalKnownBytes() {
            long total = 0L;
            for (ScriptAggregate script : scripts.values()) {
                total += script.known.totalLength();
            }
            return total;
        }

        private long totalCoveredBytes() {
            long total = 0L;
            for (ScriptAggregate script : scripts.values()) {
                total += script.covered.totalLength();
            }
            return total;
        }
    }

    private static final class ScriptAggregate {
        private final IntervalSet known = new IntervalSet();
        private final IntervalSet covered = new IntervalSet();
    }

    private static final class EffectiveRanges {
        private final IntervalSet known = new IntervalSet();
        private final IntervalSet covered = new IntervalSet();
    }

    private static final class RawRange {
        private final String functionName;
        private final long start;
        private final long end;
        private final long count;

        private RawRange(
                String functionName,
                long start,
                long end,
                long count) {
            this.functionName = functionName;
            this.start = start;
            this.end = end;
            this.count = count;
        }

        private long length() {
            return end - start;
        }
    }

    private static final class Interval {
        private final long start;
        private final long end;

        private Interval(long start, long end) {
            if (end <= start) {
                throw new IllegalArgumentException(
                        "Invalid interval: " + start + ".." + end);
            }
            this.start = start;
            this.end = end;
        }
    }

    private static final class IntervalSet {
        private final List<Interval> intervals = new ArrayList<>();

        private boolean isEmpty() {
            return intervals.isEmpty();
        }

        private void add(Interval interval) {
            addAll(Collections.singletonList(interval));
        }

        private void addAll(List<Interval> additions) {
            if (additions.isEmpty()) {
                return;
            }

            List<Interval> all = new ArrayList<>(
                    intervals.size() + additions.size());
            all.addAll(intervals);
            all.addAll(additions);
            all.sort(Comparator.comparingLong(interval -> interval.start));

            intervals.clear();
            long currentStart = all.get(0).start;
            long currentEnd = all.get(0).end;

            for (int index = 1; index < all.size(); index++) {
                Interval next = all.get(index);
                if (next.start <= currentEnd) {
                    currentEnd = Math.max(currentEnd, next.end);
                } else {
                    intervals.add(new Interval(currentStart, currentEnd));
                    currentStart = next.start;
                    currentEnd = next.end;
                }
            }

            intervals.add(new Interval(currentStart, currentEnd));
        }

        private long totalLength() {
            long total = 0L;
            for (Interval interval : intervals) {
                total += interval.end - interval.start;
            }
            return total;
        }
    }

    private static final class TestResult {
        private final String name;
        private final String status;
        private final long totalBytes;
        private final long coveredBytes;

        private TestResult(
                String name,
                String status,
                long totalBytes,
                long coveredBytes) {
            this.name = name;
            this.status = status;
            this.totalBytes = totalBytes;
            this.coveredBytes = coveredBytes;
        }
    }

    private static final class TestRawResult {
        private final String name;
        private final String status;
        private final Map<String, List<RawRange>> rawScripts;

        private TestRawResult(
                String name,
                String status,
                Map<String, List<RawRange>> rawScripts) {
            this.name = name;
            this.status = status;
            this.rawScripts = new LinkedHashMap<>();
            for (Map.Entry<String, List<RawRange>> entry
                    : rawScripts.entrySet()) {
                this.rawScripts.put(
                        entry.getKey(),
                        new ArrayList<>(entry.getValue()));
            }
        }
    }
}
