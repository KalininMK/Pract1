package emulator;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** End-to-end tests for {@link Application}. */
final class ApplicationTest {
    private final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();

    private ApplicationTest() {
    }

    /** Runs all application checks. */
    static void run() throws IOException {
        ApplicationTest test = new ApplicationTest();
        test.checkDebugOutput();
        test.checkScript();
        test.checkErrors();
    }

    /** Runs the application and returns its exit code. */
    private int start(String input, String... args) {
        outBuffer.reset();
        errBuffer.reset();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(errBuffer, true, StandardCharsets.UTF_8);
        return Application.run(args, new BufferedReader(new StringReader(input)), out, err);
    }

    /** Returns captured standard output with normalized newlines. */
    private String out() {
        return outBuffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    /** Returns captured error output with normalized newlines. */
    private String err() {
        return errBuffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    /** Verifies the debug output of all parameters. */
    private void checkDebugOutput() {
        int code = start("exit\n", "--vfs", "d/demo.json");
        Check.equal("code", Application.EXIT_OK, code);
        Check.isTrue("vfs", out().contains("[debug] --vfs = d/demo.json\n"));
        Check.isTrue("script", out().contains("[debug] --script = <not set>\n"));
        Check.isTrue("prompt", out().contains("[demo]$ "));
    }

    /** Verifies the dialog produced by a startup script. */
    private void checkScript() throws IOException {
        Path script = Files.createTempFile("startup", ".emu");
        Files.writeString(script, "// comment\nls \"a b\" // tail\n\nfoo\nexit\nls\n");
        int code = start("", "--script", script.toString());
        Files.delete(script);
        Check.equal("code", Application.EXIT_OK, code);
        String expected = "[vfs]$ ls \"a b\"\nls: [\"a b\"]\n"
                + "[vfs]$ foo\nfoo: command not found\n[vfs]$ exit\n";
        Check.isTrue("dialog", out().endsWith(expected));
        Check.isTrue("stops after exit", !out().contains("ls: []"));
    }

    /** Verifies reported errors and exit codes. */
    private void checkErrors() {
        Check.equal("usage code", Application.EXIT_USAGE, start("", "--bad"));
        Check.isTrue("usage msg", err().startsWith("error: unknown option: --bad\n"));
        int code = start("", "--script", "/no/such/script.emu");
        Check.equal("io code", Application.EXIT_IO_ERROR, code);
        Check.equal("io msg", "error: script not found: /no/such/script.emu\n", err());
    }
}
