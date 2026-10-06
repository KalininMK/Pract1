package emulator;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Выполняет стартовый скрипт и показывает его как диалог: каждая команда выводится после
 * приглашения, затем её результат.
 */
public final class ScriptRunner {
    private final Shell shell;
    private final PrintStream out;

    /**
     * Создаёт исполнитель скрипта.
     *
     * @param shell оболочка, выполняющая команды
     * @param out поток для вывода повторённого ввода
     */
    public ScriptRunner(Shell shell, PrintStream out) {
        this.shell = shell;
        this.out = out;
    }

    /**
     * Выполняет скрипт, пока он не закончится или оболочка не остановится. Пустые строки и
     * комментарии пропускаются. Ошибка команды выводится оболочкой, скрипт продолжается со
     * следующей строки.
     *
     * @param script путь к файлу скрипта
     * @throws IOException если скрипт не удаётся прочитать
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

    /** Читает все строки скрипта и поясняет ошибки чтения. */
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
