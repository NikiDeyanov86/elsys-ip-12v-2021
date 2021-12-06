package org.elsys.ip.springwebcalculator;

import java.util.ArrayList;
import java.util.List;

public class CalculatorInput {

    private String command;
    private String args;
    private List<String> result = new ArrayList<>();

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}