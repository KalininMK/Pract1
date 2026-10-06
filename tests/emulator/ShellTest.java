package emulator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Tests for {@link Shell}. */
final class ShellTest {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final Shell shell = new Shell("demo", new PrintStream(buffer, true, StandardCharsets.UTF_8));

    private ShellTest() {
    }

    /** Runs all shell checks. */
    static void run() {
        new ShellTest().checkAll();
    }

    /** Executes all checks on a fresh shell. */
    private void checkAll() {
        Check.equal("prompt", "[demo]$ ", shell.prompt());
        checkStubs();
        checkErrors();
        checkExit();
    }

    /** Returns and clears the captured output with normalized newlines. */
    private String output() {
        String text = buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
        buffer.reset();
        return text;
    }

    /** Verifies the ls and cd stubs. */
    private void checkStubs() {
        shell.execute("ls");
        Check.equal("ls no args", "ls: []\n", output());
        shell.execute("ls a \"b c\"");
        Check.equal("ls args", "ls: [\"a\", \"b c\"]\n", output());
        shell.execute("cd '/tmp/x y'");
        Check.equal("cd quoted", "cd: [\"/tmp/x y\"]\n", output());
    }

    /** Verifies error messages. */
    private void checkErrors() {
        shell.execute("foo bar");
        Check.equal("unknown", "foo: command not found\n", output());
        shell.execute("ls \"abc");
        Check.equal("syntax", "error: unterminated double quote\n", output());
        shell.execute("   ");
        Check.equal("blank", "", output());
    }

    /** Verifies the exit command. */
    private void checkExit() {
        shell.execute("exit now");
        Check.equal("exit args", "exit: too many arguments\n", output());
        Check.isTrue("still running", shell.isRunning());
        shell.execute("exit");
        Check.isTrue("stopped", !shell.isRunning());
    }
}
