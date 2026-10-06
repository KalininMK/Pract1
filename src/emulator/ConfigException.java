package emulator;

/** Signals invalid command line parameters. */
public final class ConfigException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message human readable description of the problem
     */
    public ConfigException(String message) {
        super(message);
    }
}
