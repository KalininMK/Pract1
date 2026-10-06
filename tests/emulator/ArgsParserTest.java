package emulator;

import java.nio.file.Path;
import java.util.Optional;

/** Тесты для {@link ArgsParser} и {@link Config}. */
final class ArgsParserTest {
    private ArgsParserTest() {
    }

    /** Выполняет все проверки разбора аргументов. */
    static void run() throws ConfigException {
        checkValid();
        checkVfsName();
        checkInvalid();
    }

    /** Проверяет допустимые формы аргументов. */
    private static void checkValid() throws ConfigException {
        Config empty = ArgsParser.parse(new String[] {});
        Check.equal("no vfs", Optional.empty(), empty.vfsPath());
        Check.equal("no script", Optional.empty(), empty.scriptPath());
        Config both = ArgsParser.parse(new String[] {"--vfs", "a/b.json", "--script=s.emu"});
        Check.equal("vfs", Optional.of(Path.of("a/b.json")), both.vfsPath());
        Check.equal("script", Optional.of(Path.of("s.emu")), both.scriptPath());
    }

    /** Проверяет имя VFS в приглашении. */
    private static void checkVfsName() throws ConfigException {
        Check.equal("default", "vfs", ArgsParser.parse(new String[] {}).vfsName());
        Check.equal("ext", "demo", named("dir/demo.json"));
        Check.equal("two ext", "my.vfs", named("my.vfs.json"));
        Check.equal("no ext", "image", named("/tmp/image"));
        Check.equal("hidden", ".json", named("/tmp/.json"));
    }

    /** Возвращает имя VFS для пути из командной строки. */
    private static String named(String path) throws ConfigException {
        return ArgsParser.parse(new String[] {"--vfs", path}).vfsName();
    }

    /** Проверяет отклоняемые списки аргументов. */
    private static void checkInvalid() {
        Check.isTrue("unknown", fails("--foo"));
        Check.isTrue("positional", fails("file.json"));
        Check.isTrue("missing value", fails("--vfs"));
        Check.isTrue("empty value", fails("--script="));
        Check.isTrue("duplicate", fails("--vfs", "a", "--vfs", "b"));
    }

    /** Проверяет, отклоняется ли разбор аргументов. */
    private static boolean fails(String... args) {
        try {
            ArgsParser.parse(args);
            return false;
        } catch (ConfigException e) {
            return true;
        }
    }
}
