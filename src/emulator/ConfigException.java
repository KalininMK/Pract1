package emulator;

/** Сообщает о неверных параметрах командной строки. */
public final class ConfigException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Создаёт исключение.
     *
     * @param message описание проблемы для человека
     */
    public ConfigException(String message) {
        super(message);
    }
}
