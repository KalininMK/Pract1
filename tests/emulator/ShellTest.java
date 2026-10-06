package emulator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Тесты для {@link Shell}. */
final class ShellTest {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final Shell shell = new Shell(
            "demo", new PrintStream(buffer, true, StandardCharsets.UTF_8));

    private ShellTest() {
    }

    /** Выполняет все проверки оболочки. */
    static void run() {
        new ShellTest().checkAll();
    }

    /** Выполняет все проверки на новой оболочке. */
    private void checkAll() {
        Check.equal("prompt", "[demo]$ ", shell.prompt());
        checkStubs();
        checkErrors();
        checkExit();
    }

    /** Возвращает и очищает перехваченный вывод с нормализованными переводами строк. */
    private String output() {
        String text = buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
        buffer.reset();
        return text;
    }

    /** Проверяет заглушки ls и cd. */
    private void checkStubs() {
        shell.execute("ls");
        Check.equal("ls no args", "ls: []\n", output());
        shell.execute("ls a \"b c\"");
        Check.equal("ls args", "ls: [\"a\", \"b c\"]\n", output());
        shell.execute("cd '/tmp/x y'");
        Check.equal("cd quoted", "cd: [\"/tmp/x y\"]\n", output());
        shell.execute("ls 'say \"hi\"'");
        Check.equal("ls escaped", "ls: [\"say \\\"hi\\\"\"]\n", output());
    }

    /** Проверяет сообщения об ошибках. */
    private void checkErrors() {
        shell.execute("foo bar");
        Check.equal("unknown", "foo: command not found\n", output());
        shell.execute("ls \"abc");
        Check.equal("syntax", "error: unterminated double quote\n", output());
        shell.execute("   ");
        Check.equal("blank", "", output());
    }

    /** Проверяет команду exit. */
    private void checkExit() {
        shell.execute("exit now");
        Check.equal("exit args", "exit: too many arguments\n", output());
        Check.isTrue("still running", shell.isRunning());
        shell.execute("exit");
        Check.isTrue("stopped", !shell.isRunning());
    }
}
