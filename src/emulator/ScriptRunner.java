package emulator;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Executes a startup script and shows it as a dialog: every command is
 * printed after the prompt, followed by its output.
 */
public final class ScriptRunner {
    private final Shell shell;
    private final PrintStream out;

    /**
     * Creates the runner.
     *
     * @param shell shell that executes the commands
     * @param out stream for the echoed input
     */
    public ScriptRunner(Shell shell, PrintStream out) {
        this.shell = shell;
        this.out = out;
    }

    /**
     * Runs the script until it ends or the shell stops. Blank lines and
     * comments are skipped. A failing command is reported by the shell and
     * the script continues with the next line.
     *
     * @param script path to the script file
     * @throws IOException if the script cannot be read
     */
    public void run(Path script) throws IOException {
        for (String raw : readLines(script)) {
            if (!shell.isRunning()) {
                return;
            }
            String line = CommentStripper.strip(raw).strip();
            if (line.isEmpty()) {
                continue;
            }
            out.println(shell.prompt() + line);
            shell.execute(line);
        }
    }

    /** Reads all script lines and explains read failures. */
    private static List<String> readLines(Path script) throws IOException {
        try {
            return Files.readAllLines(script, StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            throw new IOException("script not found: " + script, e);
        } catch (IOException e) {
            throw new IOException("cannot read script " + script + ": " + e.getMessage(), e);
        }
    }
}
