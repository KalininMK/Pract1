package emulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Разбивает строку ввода на токены.
 *
 * <p>Токены разделяются пробельными символами. Одинарные кавычки сохраняют всё как есть, двойные
 * допускают экранирование {@code \"} и {@code \\}, а обратная косая черта вне кавычек экранирует
 * следующий символ.
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
     * Разбивает строку на токены.
     *
     * @param line исходная строка ввода
     * @return список токенов, пустой для пустой строки
     * @throws ParseException если кавычка или экранирование не завершены
     */
    public static List<String> tokenize(String line) throws ParseException {
        return new Tokenizer(line).run();
    }

    /** Просматривает всю строку и возвращает собранные токены. */
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

    /** Добавляет символ к текущему токену. */
    private void append(char c) {
        current.append(c);
        inToken = true;
    }

    /** Завершает текущий токен, если он есть. */
    private void flush() {
        if (inToken) {
            tokens.add(current.toString());
            current.setLength(0);
            inToken = false;
        }
    }

    /** Читает текст до закрывающей одинарной кавычки. */
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

    /** Читает текст до закрывающей двойной кавычки. */
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

    /** Обрабатывает обратную косую черту внутри двойных кавычек. */
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

    /** Обрабатывает обратную косую черту вне кавычек. */
    private void readEscaped() throws ParseException {
        if (pos >= line.length()) {
            throw new ParseException("dangling backslash at end of line");
        }
        append(line.charAt(pos++));
    }
}
