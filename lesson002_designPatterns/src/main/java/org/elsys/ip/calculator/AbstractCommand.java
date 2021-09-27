package org.elsys.ip.calculator;

public abstract class AbstractCommand implements Command {
    protected double parseValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch(Throwable t) {
            return Memory.getInstance().getVariable(value);
        }
    }
}
