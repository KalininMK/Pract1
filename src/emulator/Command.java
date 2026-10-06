package emulator;

import java.util.List;

/** A shell command that can be executed with a list of arguments. */
@FunctionalInterface
public interface Command {
    /**
     * Runs the command.
     *
     * @param args arguments without the command name
     */
    void run(List<String> args);
}
