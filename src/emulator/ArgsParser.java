package emulator;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Parses command line arguments into a {@link Config}. */
public final class ArgsParser {
    /** Option with the physical path to the VFS. */
    public static final String VFS_OPTION = "--vfs";
    /** Option with the path to the startup script. */
    public static final String SCRIPT_OPTION = "--script";
    /** Short usage text. */
    public static final String USAGE =
            "usage: emulator [--vfs <path>] [--script <path>]";

    private static final Set<String> KNOWN_OPTIONS = Set.of(VFS_OPTION, SCRIPT_OPTION);
    private static final char VALUE_SEPARATOR = '=';
    private static final int NOT_FOUND = -1;

    private ArgsParser() {
    }

    /**
     * Parses the arguments. Options accept both {@code --name value} and
     * {@code --name=value} forms.
     *
     * @param args raw command line arguments
     * @return parsed configuration
     * @throws ConfigException on unknown, duplicated or incomplete options
     */
    public static Config parse(String[] args) throws ConfigException {
        Map<String, String> values = new HashMap<>();
        int index = 0;
        while (index < args.length) {
            index = readOption(args, index, values);
        }
        return new Config(
                toPath(VFS_OPTION, values.get(VFS_OPTION)),
                toPath(SCRIPT_OPTION, values.get(SCRIPT_OPTION)));
    }

    /** Reads one option and returns the index of the next unread argument. */
    private static int readOption(String[] args, int index, Map<String, String> values)
            throws ConfigException {
        String arg = args[index];
        int separator = arg.indexOf(VALUE_SEPARATOR);
        String name = separator == NOT_FOUND ? arg : arg.substring(0, separator);
        if (!KNOWN_OPTIONS.contains(name)) {
            throw new ConfigException("unknown option: " + arg);
        }
        if (values.containsKey(name)) {
            throw new ConfigException("duplicate option: " + name);
        }
        if (separator != NOT_FOUND) {
            values.put(name, arg.substring(separator + 1));
            return index + 1;
        }
        int valueIndex = index + 1;
        if (valueIndex >= args.length) {
            throw new ConfigException("option " + name + " requires a value");
        }
        values.put(name, args[valueIndex]);
        return valueIndex + 1;
    }

    /** Converts an optional raw value to a path. */
    private static Optional<Path> toPath(String option, String raw) throws ConfigException {
        if (raw == null) {
            return Optional.empty();
        }
        if (raw.isEmpty()) {
            throw new ConfigException("option " + option + " requires a non-empty value");
        }
        try {
            return Optional.of(Path.of(raw));
        } catch (InvalidPathException e) {
            throw new ConfigException("invalid path for " + option + ": " + raw);
        }
    }
}
