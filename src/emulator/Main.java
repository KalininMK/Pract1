package emulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Entry point of the shell emulator. */
public final class Main {
    private Main() {
    }

    /**
     * Starts the emulator with the standard streams.
     *
     * @param args command line arguments: {@code --vfs} and {@code --script}
     */
    public static void main(String[] args) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(System.err, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.exit(Application.run(args, in, out, err));
    }
}
