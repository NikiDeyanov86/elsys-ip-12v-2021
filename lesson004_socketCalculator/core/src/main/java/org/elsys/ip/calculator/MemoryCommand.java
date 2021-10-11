package org.elsys.ip.calculator;

import java.util.List;

public class MemoryCommand extends AbstractCommand implements Command {
    public MemoryCommand(Memory memory) {
        super(memory);
    }

    @Override
    public String execute(List<String> args) {
        if (args.size() != 2) {
            throw new IllegalArgumentException("args is not with size 2");
        }
        String variable = args.get(0);
        double value = parseValue(args.get(1));
        memory.setVariable(variable, value);
        return variable + " set to " + value;
    }
}
