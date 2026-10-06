package emulator;

import java.util.List;

/** Тесты для {@link Tokenizer}. */
final class TokenizerTest {
    private TokenizerTest() {
    }

    /** Выполняет все проверки токенизатора. */
    static void run() throws ParseException {
        checkPlainSplit();
        checkQuotes();
        checkEscapes();
        checkErrors();
    }

    /** Проверяет разбиение по пробельным символам. */
    private static void checkPlainSplit() throws ParseException {
        Check.equal("simple", List.of("ls", "-l", "dir"), Tokenizer.tokenize("ls -l dir"));
        Check.equal("spaces", List.of("a", "b"), Tokenizer.tokenize("  a \t  b  "));
        Check.equal("blank", List.of(), Tokenizer.tokenize("   "));
    }

    /** Проверяет аргументы в кавычках. */
    private static void checkQuotes() throws ParseException {
        Check.equal("double", List.of("cd", "my dir"), Tokenizer.tokenize("cd \"my dir\""));
        Check.equal("single", List.of("cd", "my dir"), Tokenizer.tokenize("cd 'my dir'"));
        Check.equal("empty", List.of("echo", ""), Tokenizer.tokenize("echo \"\""));
        Check.equal("glued", List.of("ab cd"), Tokenizer.tokenize("a\"b c\"d"));
        Check.equal("nested", List.of("it's"), Tokenizer.tokenize("\"it's\""));
    }

    /** Проверяет обработку обратной косой черты. */
    private static void checkEscapes() throws ParseException {
        Check.equal("space", List.of("a b"), Tokenizer.tokenize("a\\ b"));
        Check.equal("quote", List.of("say \"hi\""), Tokenizer.tokenize("\"say \\\"hi\\\"\""));
        Check.equal("literal", List.of("a\\nb"), Tokenizer.tokenize("\"a\\nb\""));
        Check.equal("single", List.of("a\\b"), Tokenizer.tokenize("'a\\b'"));
    }

    /** Проверяет, что неверные кавычки отклоняются. */
    private static void checkErrors() {
        Check.isTrue("open double", fails("ls \"abc"));
        Check.isTrue("open single", fails("ls 'abc"));
        Check.isTrue("dangling", fails("ls abc\\"));
    }

    /** Проверяет, приводит ли токенизация строки к ошибке разбора. */
    private static boolean fails(String line) {
        try {
            Tokenizer.tokenize(line);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
