package emulator;

/** Запускает все тесты и завершается с ненулевым кодом при ошибке. */
public final class TestMain {
    private TestMain() {
    }

    /**
     * Точка входа запуска тестов.
     *
     * @param args не используются
     * @throws Exception если тест неожиданно завершился ошибкой
     */
    public static void main(String[] args) throws Exception {
        TokenizerTest.run();
        ShellTest.run();
        ArgsParserTest.run();
        CommentStripperTest.run();
        ApplicationTest.run();
        System.exit(Check.finish(System.out));
    }
}
