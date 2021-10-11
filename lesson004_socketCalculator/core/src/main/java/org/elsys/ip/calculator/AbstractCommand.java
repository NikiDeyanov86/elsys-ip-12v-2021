package org.elsys.ip.calculator;

public abstract class AbstractCommand implements Command {
    protected final Memory memory;

    protected AbstractCommand(Memory memory) {
        this.memory = memory;
    }

    protected double parseValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch(Throwable t) {
            return memory.getVariable(value);
        }
    }
}
