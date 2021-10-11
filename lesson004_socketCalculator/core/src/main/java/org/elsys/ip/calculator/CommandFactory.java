package org.elsys.ip.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandFactory {
    private final Map<String, Supplier<Command>> commandMap;

    public CommandFactory(Memory memory) {
        commandMap = new HashMap<String, Supplier<Command>>() {{
            put("add", () -> new AddCommand(memory));
            put("sub", () -> new SubCommand(memory));
            put("mem", () -> new MemoryCommand(memory));
            put("exit", () -> new ExitCommand());
        }};
    }

    public Command createCommand(String name) {
        Supplier<Command> commandSupplier =
                commandMap.get(name.toLowerCase());
        if (commandSupplier == null) {
            return null;
        }
            return commandSupplier.get();
    }
}
