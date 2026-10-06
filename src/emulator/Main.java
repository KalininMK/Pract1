package emulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Точка входа эмулятора оболочки. */
public final class Main {
    private Main() {
    }

    /**
     * Запускает эмулятор со стандартными потоками.
     *
     * @param args аргументы командной строки: {@code --vfs} и {@code --script}
     */
    public static void main(String[] args) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(System.err, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.exit(Application.run(args, in, out, err));
    }
}
