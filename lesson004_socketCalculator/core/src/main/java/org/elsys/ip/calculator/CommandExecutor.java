package org.elsys.ip.calculator;

import java.util.List;

public class CommandExecutor {
    private final CommandFactory factory;

    public CommandExecutor(CommandFactory commandFactory) {
        factory = commandFactory;
    }

    public String execute(
            String name,
            List<String> args) {

        Command command = factory.createCommand(name);
        if (command != null) {
            return command.execute(args);
        } else {
            return "A command with name " + name + " couldn't be found.";
        }
    }
}
