package emulator;

/**
 * Removes {@code //} comments from script lines. A comment starts at a
 * {@code //} that is at the beginning of a word and is not inside quotes.
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
     * Cuts the comment off a line.
     *
     * @param line script line
     * @return the line without comment
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

    /** Tells whether a comment begins at the given position. */
    private static boolean startsComment(String line, int index) {
        boolean wordStart = index == 0 || Character.isWhitespace(line.charAt(index - 1));
        return wordStart && line.startsWith(COMMENT_MARKER, index);
    }
}
