package emulator;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Параметры одного запуска эмулятора.
 *
 * @param vfsPath физическое расположение VFS, если задано
 * @param scriptPath путь к стартовому скрипту, если задан
 */
public record Config(Optional<Path> vfsPath, Optional<Path> scriptPath) {
    private static final String DEFAULT_VFS_NAME = "vfs";
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * Возвращает имя VFS для приглашения: имя файла VFS без последнего расширения или {@code vfs},
     * если VFS не задана.
     */
    public String vfsName() {
        return vfsPath
                .map(Path::getFileName)
                .map(Path::toString)
                .map(Config::stripExtension)
                .filter(name -> !name.isEmpty())
                .orElse(DEFAULT_VFS_NAME);
    }

    /** Удаляет последнее расширение из имени файла. */
    private static String stripExtension(String fileName) {
        int dot = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        return dot > 0 ? fileName.substring(0, dot) : fileName;
    }
}
