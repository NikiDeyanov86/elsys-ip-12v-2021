package org.elsys.ip.calculator;

import java.util.List;

public class MemoryCommand extends AbstractCommand implements Command {
    @Override
    public String execute(List<String> args) {
        if (args.size() != 2) {
            throw new IllegalArgumentException("args is not with size 2");
        }
        String variable = args.get(0);
        double value = parseValue(args.get(1));
        Memory.getInstance().setVariable(variable, value);
        return variable + " set to " + value;
    }
}
