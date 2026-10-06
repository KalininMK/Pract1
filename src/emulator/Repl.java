package emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/** Interactive read-eval-print loop on top of a {@link Shell}. */
public final class Repl {
    private final Shell shell;
    private final BufferedReader in;
    private final PrintStream out;

    /**
     * Creates the loop.
     *
     * @param shell shell that executes the lines
     * @param in source of user input
     * @param out stream for the prompt
     */
    public Repl(Shell shell, BufferedReader in, PrintStream out) {
        this.shell = shell;
        this.in = in;
        this.out = out;
    }

    /**
     * Reads and executes lines until the shell stops or the input ends.
     *
     * @throws IOException if reading the input fails
     */
    public void run() throws IOException {
        while (shell.isRunning()) {
            out.print(shell.prompt());
            out.flush();
            String line = in.readLine();
            if (line == null) {
                out.println();
                return;
            }
            shell.execute(line);
        }
    }
}
