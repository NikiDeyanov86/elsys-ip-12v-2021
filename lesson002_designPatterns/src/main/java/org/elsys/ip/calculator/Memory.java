package org.elsys.ip.calculator;

import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Memory {
    private Memory() {}
    private static Memory instace;
    public static Memory getInstance() {
        if (instace == null) {
            instace = new Memory();
        }

        return instace;
    }

    private final Map<String, Double> memory =
            new HashMap<>();

    public double getVariable(String name) {
        return memory.getOrDefault(name, 0.0);
    }

    public void setVariable(String name, double value) {
        memory.put(name, value);
    }
}
