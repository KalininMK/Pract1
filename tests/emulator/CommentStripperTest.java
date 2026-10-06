package emulator;

/** Tests for {@link CommentStripper}. */
final class CommentStripperTest {
    private CommentStripperTest() {
    }

    /** Runs all comment stripping checks. */
    static void run() {
        Check.equal("full line", "", CommentStripper.strip("// note"));
        Check.equal("trailing", "ls -l ", CommentStripper.strip("ls -l // list"));
        Check.equal("no comment", "ls a", CommentStripper.strip("ls a"));
        Check.equal("in double", "ls \"a // b\"", CommentStripper.strip("ls \"a // b\""));
        Check.equal("in single", "ls 'a // b'", CommentStripper.strip("ls 'a // b'"));
        Check.equal("inside word", "cd http://x", CommentStripper.strip("cd http://x"));
        Check.equal("escaped quote", "ls \\\" ", CommentStripper.strip("ls \\\" // c"));
    }
}
