package org.elsys.ip.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandFactory {
    private Map<String, Supplier<Command>> commandMap =
            new HashMap<>() {{
                put("add", () -> new AddCommand());
                put("sub", () -> new SubCommand());
                put("mem", () -> new MemoryCommand());
                put("exit", () -> new ExitCommand());
            }};

    public Command createCommand(String name) {
        Supplier<Command> commandSupplier =
                commandMap.get(name.toLowerCase());
        if (commandSupplier == null) {
            return null;
        }
            return commandSupplier.get();
    }
}
