package emulator;

/** Signals that an input line cannot be split into command and arguments. */
public final class ParseException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message human readable description of the syntax error
     */
    public ParseException(String message) {
        super(message);
    }
}
