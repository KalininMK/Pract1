package emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/** Связывает конфигурацию, стартовый скрипт и интерактивный цикл. */
public final class Application {
    /** Код возврата при штатном завершении. */
    public static final int EXIT_OK = 0;
    /** Код возврата при ошибке чтения стартового скрипта. */
    public static final int EXIT_IO_ERROR = 1;
    /** Код возврата при неверных параметрах командной строки. */
    public static final int EXIT_USAGE = 2;

    private Application() {
    }

    /**
     * Запускает эмулятор.
     *
     * @param args аргументы командной строки
     * @param in источник интерактивного ввода
     * @param out поток для обычного вывода
     * @param err поток для критических ошибок
     * @return код завершения процесса
     */
    public static int run(String[] args, BufferedReader in, PrintStream out, PrintStream err) {
        Config config;
        try {
            config = ArgsParser.parse(args);
        } catch (ConfigException e) {
            err.println("error: " + e.getMessage());
            err.println(ArgsParser.USAGE);
            return EXIT_USAGE;
        }
        DebugPrinter.print(config, out);
        Shell shell = new Shell(config.vfsName(), out);
        try {
            if (config.scriptPath().isPresent()) {
                new ScriptRunner(shell, out).run(config.scriptPath().get());
            }
            new Repl(shell, in, out).run();
        } catch (IOException e) {
            err.println("error: " + e.getMessage());
            return EXIT_IO_ERROR;
        }
        return EXIT_OK;
    }
}
