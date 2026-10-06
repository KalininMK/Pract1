package emulator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Minimal assertion helper that records failures instead of throwing. */
final class Check {
    private static final int EXIT_OK = 0;
    private static final int EXIT_FAILED = 1;
    private static final List<String> FAILURES = new ArrayList<>();
    private static int total;

    private Check() {
    }

    /** Compares two values and records a failure if they differ. */
    static void equal(String label, Object expected, Object actual) {
        total++;
        if (!Objects.equals(expected, actual)) {
            FAILURES.add(label + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }

    /** Records a failure if the condition is false. */
    static void isTrue(String label, boolean condition) {
        equal(label, true, condition);
    }

    /** Prints the summary and returns the process exit code. */
    static int finish(PrintStream out) {
        FAILURES.forEach(failure -> out.println("FAIL " + failure));
        out.println("checks: " + total + ", failed: " + FAILURES.size());
        return FAILURES.isEmpty() ? EXIT_OK : EXIT_FAILED;
    }
}
