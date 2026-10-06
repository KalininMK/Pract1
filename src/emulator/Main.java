package emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Entry point of the shell emulator. */
public final class Main {
    private static final String DEFAULT_VFS_NAME = "vfs";

    private Main() {
    }

    /**
     * Starts the interactive emulator.
     *
     * @param args command line arguments (unused at this stage)
     * @throws IOException if reading the standard input fails
     */
    public static void main(String[] args) throws IOException {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        Shell shell = new Shell(DEFAULT_VFS_NAME, out);
        new Repl(shell, in, out).run();
    }
}
