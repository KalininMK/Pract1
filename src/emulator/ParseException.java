package emulator;

/** Сообщает, что строку ввода нельзя разбить на команду и аргументы. */
public final class ParseException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение.
     *
     * @param message описание синтаксической ошибки для человека
     */
    public ParseException(String message) {
        super(message);
    }
}
