package emulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Splits an input line into tokens.
 *
 * <p>Tokens are separated by whitespace. Single quotes keep everything
 * literally, double quotes allow {@code \"} and {@code \\} escapes, and a
 * backslash outside quotes escapes the next character.
 */
public final class Tokenizer {
    private static final char DOUBLE_QUOTE = '"';
    private static final char SINGLE_QUOTE = '\'';
    private static final char ESCAPE = '\\';

    private final String line;
    private final List<String> tokens = new ArrayList<>();
    private final StringBuilder current = new StringBuilder();
    private boolean inToken;
    private int pos;

    private Tokenizer(String line) {
        this.line = line;
    }

    /**
     * Splits the line into tokens.
     *
     * @param line raw input line
     * @return list of tokens, empty for a blank line
     * @throws ParseException if a quote or an escape is not terminated
     */
    public static List<String> tokenize(String line) throws ParseException {
        return new Tokenizer(line).run();
    }

    /** Scans the whole line and returns the collected tokens. */
    private List<String> run() throws ParseException {
        while (pos < line.length()) {
            char c = line.charAt(pos++);
            if (Character.isWhitespace(c)) {
                flush();
            } else if (c == SINGLE_QUOTE) {
                readSingleQuoted();
            } else if (c == DOUBLE_QUOTE) {
                readDoubleQuoted();
            } else if (c == ESCAPE) {
                readEscaped();
            } else {
                append(c);
            }
        }
        flush();
        return tokens;
    }

    /** Appends a character to the current token. */
    private void append(char c) {
        current.append(c);
        inToken = true;
    }

    /** Finishes the current token, if there is one. */
    private void flush() {
        if (inToken) {
            tokens.add(current.toString());
            current.setLength(0);
            inToken = false;
        }
    }

    /** Reads text up to the closing single quote. */
    private void readSingleQuoted() throws ParseException {
        inToken = true;
        while (pos < line.length()) {
            char c = line.charAt(pos++);
            if (c == SINGLE_QUOTE) {
                return;
            }
            current.append(c);
        }
        throw new ParseException("unterminated single quote");
    }

    /** Reads text up to the closing double quote. */
    private void readDoubleQuoted() throws ParseException {
        inToken = true;
        while (pos < line.length()) {
            char c = line.charAt(pos++);
            if (c == DOUBLE_QUOTE) {
                return;
            }
            if (c == ESCAPE) {
                readEscapedInQuotes();
            } else {
                current.append(c);
            }
        }
        throw new ParseException("unterminated double quote");
    }

    /** Handles a backslash that appears inside double quotes. */
    private void readEscapedInQuotes() throws ParseException {
        if (pos >= line.length()) {
            throw new ParseException("unterminated double quote");
        }
        char next = line.charAt(pos++);
        if (next != DOUBLE_QUOTE && next != ESCAPE) {
            current.append(ESCAPE);
        }
        current.append(next);
    }

    /** Handles a backslash that appears outside quotes. */
    private void readEscaped() throws ParseException {
        if (pos >= line.length()) {
            throw new ParseException("dangling backslash at end of line");
        }
        append(line.charAt(pos++));
    }
}
