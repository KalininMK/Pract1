package emulator;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Выполняет командные строки и хранит состояние эмулируемой оболочки. */
public final class Shell {
    private final String vfsName;
    private final PrintStream out;
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private boolean running = true;

    /**
     * Создаёт оболочку и регистрирует встроенные команды.
     *
     * @param vfsName имя, показываемое в приглашении
     * @param out поток для вывода команд и сообщений об ошибках
     */
    public Shell(String vfsName, PrintStream out) {
        this.vfsName = vfsName;
        this.out = out;
        commands.put("ls", args -> printStub("ls", args));
        commands.put("cd", args -> printStub("cd", args));
        commands.put("exit", this::exit);
    }

    /** Возвращает приглашение к вводу, содержащее имя VFS. */
    public String prompt() {
        return "[" + vfsName + "]$ ";
    }

    /** Проверяет, принимает ли оболочка новые команды. */
    public boolean isRunning() {
        return running;
    }

    /**
     * Разбирает и выполняет одну строку ввода. Ошибки выводятся в выходной поток и не останавливают
     * оболочку.
     *
     * @param line исходная строка ввода
     */
    public void execute(String line) {
        List<String> tokens;
        try {
            tokens = Tokenizer.tokenize(line);
        } catch (ParseException e) {
            out.println("error: " + e.getMessage());
            return;
        }
        if (tokens.isEmpty()) {
            return;
        }
        String name = tokens.get(0);
        Command command = commands.get(name);
        if (command == null) {
            out.println(name + ": command not found");
            return;
        }
        command.run(tokens.subList(1, tokens.size()));
    }

    /** Выводит имя команды и её аргументы. */
    private void printStub(String name, List<String> args) {
        String joined = args.stream()
                .map(Shell::quote)
                .collect(Collectors.joining(", "));
        out.println(name + ": [" + joined + "]");
    }

    /** Заключает аргумент в двойные кавычки и экранирует кавычки и обратные косые черты. */
    private static String quote(String arg) {
        return "\"" + arg.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    /** Останавливает оболочку. */
    private void exit(List<String> args) {
        if (!args.isEmpty()) {
            out.println("exit: too many arguments");
            return;
        }
        running = false;
    }
}
