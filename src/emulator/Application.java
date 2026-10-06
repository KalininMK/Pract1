package emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/** Wires configuration, startup script and interactive loop together. */
public final class Application {
    /** Exit code of a normal termination. */
    public static final int EXIT_OK = 0;
    /** Exit code when reading the startup script fails. */
    public static final int EXIT_IO_ERROR = 1;
    /** Exit code for invalid command line parameters. */
    public static final int EXIT_USAGE = 2;

    private Application() {
    }

    /**
     * Runs the emulator.
     *
     * @param args command line arguments
     * @param in source of interactive input
     * @param out stream for normal output
     * @param err stream for fatal errors
     * @return process exit code
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
