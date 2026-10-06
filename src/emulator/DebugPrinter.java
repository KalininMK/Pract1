package emulator;

import java.io.PrintStream;

/** Выводит действующие параметры эмулятора при запуске. */
public final class DebugPrinter {
    private static final String NOT_SET = "<not set>";

    private DebugPrinter() {
    }

    /**
     * Выводит все параметры, по одному в строке.
     *
     * @param config параметры запуска
     * @param out поток вывода
     */
    public static void print(Config config, PrintStream out) {
        out.println("[debug] " + ArgsParser.VFS_OPTION + " = "
                + config.vfsPath().map(Object::toString).orElse(NOT_SET));
        out.println("[debug] " + ArgsParser.SCRIPT_OPTION + " = "
                + config.scriptPath().map(Object::toString).orElse(NOT_SET));
        out.println("[debug] vfs name = " + config.vfsName());
    }
}
