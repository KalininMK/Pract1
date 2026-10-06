package emulator;

import java.util.List;

/** Команда оболочки, выполняемая со списком аргументов. */
@FunctionalInterface
public interface Command {
    /**
     * Выполняет команду.
     *
     * @param args аргументы без имени команды
     */
    void run(List<String> args);
}
