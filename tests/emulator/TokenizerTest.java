package emulator;

import java.util.List;

/** Tests for {@link Tokenizer}. */
final class TokenizerTest {
    private TokenizerTest() {
    }

    /** Runs all tokenizer checks. */
    static void run() throws ParseException {
        checkPlainSplit();
        checkQuotes();
        checkEscapes();
        checkErrors();
    }

    /** Verifies splitting on whitespace. */
    private static void checkPlainSplit() throws ParseException {
        Check.equal("simple", List.of("ls", "-l", "dir"), Tokenizer.tokenize("ls -l dir"));
        Check.equal("spaces", List.of("a", "b"), Tokenizer.tokenize("  a \t  b  "));
        Check.equal("blank", List.of(), Tokenizer.tokenize("   "));
    }

    /** Verifies quoted arguments. */
    private static void checkQuotes() throws ParseException {
        Check.equal("double", List.of("cd", "my dir"), Tokenizer.tokenize("cd \"my dir\""));
        Check.equal("single", List.of("cd", "my dir"), Tokenizer.tokenize("cd 'my dir'"));
        Check.equal("empty", List.of("echo", ""), Tokenizer.tokenize("echo \"\""));
        Check.equal("glued", List.of("ab cd"), Tokenizer.tokenize("a\"b c\"d"));
        Check.equal("nested", List.of("it's"), Tokenizer.tokenize("\"it's\""));
    }

    /** Verifies backslash handling. */
    private static void checkEscapes() throws ParseException {
        Check.equal("space", List.of("a b"), Tokenizer.tokenize("a\\ b"));
        Check.equal("quote", List.of("say \"hi\""), Tokenizer.tokenize("\"say \\\"hi\\\"\""));
        Check.equal("literal", List.of("a\\nb"), Tokenizer.tokenize("\"a\\nb\""));
        Check.equal("single", List.of("a\\b"), Tokenizer.tokenize("'a\\b'"));
    }

    /** Verifies that broken quoting is rejected. */
    private static void checkErrors() {
        Check.isTrue("open double", fails("ls \"abc"));
        Check.isTrue("open single", fails("ls 'abc"));
        Check.isTrue("dangling", fails("ls abc\\"));
    }

    /** Tells whether tokenizing the line throws a parse error. */
    private static boolean fails(String line) {
        try {
            Tokenizer.tokenize(line);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
