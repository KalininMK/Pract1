package emulator;

import java.nio.file.Path;
import java.util.Optional;

/** Tests for {@link ArgsParser} and {@link Config}. */
final class ArgsParserTest {
    private ArgsParserTest() {
    }

    /** Runs all argument parser checks. */
    static void run() throws ConfigException {
        checkValid();
        checkVfsName();
        checkInvalid();
    }

    /** Verifies accepted argument forms. */
    private static void checkValid() throws ConfigException {
        Config empty = ArgsParser.parse(new String[] {});
        Check.equal("no vfs", Optional.empty(), empty.vfsPath());
        Check.equal("no script", Optional.empty(), empty.scriptPath());
        Config both = ArgsParser.parse(new String[] {"--vfs", "a/b.json", "--script=s.emu"});
        Check.equal("vfs", Optional.of(Path.of("a/b.json")), both.vfsPath());
        Check.equal("script", Optional.of(Path.of("s.emu")), both.scriptPath());
    }

    /** Verifies the VFS name shown in the prompt. */
    private static void checkVfsName() throws ConfigException {
        Check.equal("default", "vfs", ArgsParser.parse(new String[] {}).vfsName());
        Check.equal("ext", "demo", named("dir/demo.json"));
        Check.equal("two ext", "my.vfs", named("my.vfs.json"));
        Check.equal("no ext", "image", named("/tmp/image"));
        Check.equal("hidden", ".json", named("/tmp/.json"));
    }

    /** Returns the VFS name for a path given on the command line. */
    private static String named(String path) throws ConfigException {
        return ArgsParser.parse(new String[] {"--vfs", path}).vfsName();
    }

    /** Verifies rejected argument lists. */
    private static void checkInvalid() {
        Check.isTrue("unknown", fails("--foo"));
        Check.isTrue("positional", fails("file.json"));
        Check.isTrue("missing value", fails("--vfs"));
        Check.isTrue("empty value", fails("--script="));
        Check.isTrue("duplicate", fails("--vfs", "a", "--vfs", "b"));
    }

    /** Tells whether parsing the arguments is rejected. */
    private static boolean fails(String... args) {
        try {
            ArgsParser.parse(args);
            return false;
        } catch (ConfigException e) {
            return true;
        }
    }
}
