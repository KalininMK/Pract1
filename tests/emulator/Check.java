package emulator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Минимальный помощник для проверок: записывает ошибки вместо выброса исключений. */
final class Check {
    private static final int EXIT_OK = 0;
    private static final int EXIT_FAILED = 1;
    private static final List<String> FAILURES = new ArrayList<>();
    private static int total;

    private Check() {
    }

    /** Сравнивает два значения и фиксирует ошибку, если они различаются. */
    static void equal(String label, Object expected, Object actual) {
        total++;
        if (!Objects.equals(expected, actual)) {
            FAILURES.add(label + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }

    /** Фиксирует ошибку, если условие ложно. */
    static void isTrue(String label, boolean condition) {
        equal(label, true, condition);
    }

    /** Выводит итог и возвращает код завершения процесса. */
    static int finish(PrintStream out) {
        FAILURES.forEach(failure -> out.println("FAIL " + failure));
        out.println("checks: " + total + ", failed: " + FAILURES.size());
        return FAILURES.isEmpty() ? EXIT_OK : EXIT_FAILED;
    }
}
