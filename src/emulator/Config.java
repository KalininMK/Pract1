package emulator;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Parameters of one emulator run.
 *
 * @param vfsPath physical location of the VFS, if given
 * @param scriptPath path to the startup script, if given
 */
public record Config(Optional<Path> vfsPath, Optional<Path> scriptPath) {
    private static final String DEFAULT_VFS_NAME = "vfs";
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * Returns the VFS name used in the prompt: the file name of the VFS
     * without its last extension, or {@code vfs} when no VFS is given.
     */
    public String vfsName() {
        return vfsPath
                .map(Path::getFileName)
                .map(Path::toString)
                .map(Config::stripExtension)
                .filter(name -> !name.isEmpty())
                .orElse(DEFAULT_VFS_NAME);
    }

    /** Removes the last extension from a file name. */
    private static String stripExtension(String fileName) {
        int dot = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        return dot > 0 ? fileName.substring(0, dot) : fileName;
    }
}
