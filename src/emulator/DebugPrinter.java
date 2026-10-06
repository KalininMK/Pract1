package emulator;

import java.io.PrintStream;

/** Prints the effective emulator parameters at startup. */
public final class DebugPrinter {
    private static final String NOT_SET = "<not set>";

    private DebugPrinter() {
    }

    /**
     * Prints all parameters, one per line.
     *
     * @param config parameters of the run
     * @param out destination stream
     */
    public static void print(Config config, PrintStream out) {
        out.println("[debug] " + ArgsParser.VFS_OPTION + " = "
                + config.vfsPath().map(Object::toString).orElse(NOT_SET));
        out.println("[debug] " + ArgsParser.SCRIPT_OPTION + " = "
                + config.scriptPath().map(Object::toString).orElse(NOT_SET));
        out.println("[debug] vfs name = " + config.vfsName());
    }
}
