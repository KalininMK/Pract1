package emulator;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Executes command lines and keeps the state of the emulated shell. */
public final class Shell {
    private final String vfsName;
    private final PrintStream out;
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private boolean running = true;

    /**
     * Creates a shell and registers the built-in commands.
     *
     * @param vfsName name shown in the prompt
     * @param out stream for command output and error messages
     */
    public Shell(String vfsName, PrintStream out) {
        this.vfsName = vfsName;
        this.out = out;
        commands.put("ls", args -> printStub("ls", args));
        commands.put("cd", args -> printStub("cd", args));
        commands.put("exit", this::exit);
    }

    /** Returns the input prompt, which contains the VFS name. */
    public String prompt() {
        return "[" + vfsName + "]$ ";
    }

    /** Tells whether the shell accepts more commands. */
    public boolean isRunning() {
        return running;
    }

    /**
     * Parses and executes one input line. Errors are reported to the output
     * stream and never abort the shell.
     *
     * @param line raw input line
     */
    public void execute(String line) {
        List<String> tokens;
        try {
            tokens = Tokenizer.tokenize(line);
        } catch (ParseException e) {
            out.println("error: " + e.getMessage());
            return;
        }
        if (tokens.isEmpty()) {
            return;
        }
        String name = tokens.get(0);
        Command command = commands.get(name);
        if (command == null) {
            out.println(name + ": command not found");
            return;
        }
        command.run(tokens.subList(1, tokens.size()));
    }

    /** Prints the command name and its arguments. */
    private void printStub(String name, List<String> args) {
        String joined = args.stream()
                .map(arg -> "\"" + arg + "\"")
                .collect(Collectors.joining(", "));
        out.println(name + ": [" + joined + "]");
    }

    /** Stops the shell. */
    private void exit(List<String> args) {
        if (!args.isEmpty()) {
            out.println("exit: too many arguments");
            return;
        }
        running = false;
    }
}
