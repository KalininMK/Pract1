package emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/** Интерактивный цикл «чтение — выполнение — вывод» поверх {@link Shell}. */
public final class Repl {
    private final Shell shell;
    private final BufferedReader in;
    private final PrintStream out;

    /**
     * Создаёт цикл.
     *
     * @param shell оболочка, выполняющая строки
     * @param in источник пользовательского ввода
     * @param out поток для приглашения
     */
    public Repl(Shell shell, BufferedReader in, PrintStream out) {
        this.shell = shell;
        this.in = in;
        this.out = out;
    }

    /**
     * Читает и выполняет строки, пока оболочка не остановится или ввод не закончится.
     *
     * @throws IOException если чтение ввода не удалось
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
