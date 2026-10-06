package emulator;

/** Runs all tests and exits with a non-zero code on failure. */
public final class TestMain {
    private TestMain() {
    }

    /**
     * Entry point of the test run.
     *
     * @param args unused
     * @throws Exception if a test fails unexpectedly
     */
    public static void main(String[] args) throws Exception {
        TokenizerTest.run();
        ShellTest.run();
        System.exit(Check.finish(System.out));
    }
}
