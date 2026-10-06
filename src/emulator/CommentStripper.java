package emulator;

/**
 * Удаляет комментарии {@code //} из строк скрипта. Комментарий начинается с {@code //} в начале
 * слова вне кавычек.
 */
public final class CommentStripper {
    private static final String COMMENT_MARKER = "//";
    private static final char DOUBLE_QUOTE = '"';
    private static final char SINGLE_QUOTE = '\'';
    private static final char ESCAPE = '\\';
    private static final char NO_QUOTE = '\0';

    private CommentStripper() {
    }

    /**
     * Отрезает комментарий от строки.
     *
     * @param line строка скрипта
     * @return строка без комментария
     */
    public static String strip(String line) {
        char quote = NO_QUOTE;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (quote != NO_QUOTE) {
                quote = c == quote ? NO_QUOTE : quote;
            } else if (c == DOUBLE_QUOTE || c == SINGLE_QUOTE) {
                quote = c;
            } else if (c == ESCAPE) {
                i++;
            } else if (startsComment(line, i)) {
                return line.substring(0, i);
            }
        }
        return line;
    }

    /** Проверяет, начинается ли комментарий в данной позиции. */
    private static boolean startsComment(String line, int index) {
        boolean wordStart = index == 0 || Character.isWhitespace(line.charAt(index - 1));
        return wordStart && line.startsWith(COMMENT_MARKER, index);
    }
}
