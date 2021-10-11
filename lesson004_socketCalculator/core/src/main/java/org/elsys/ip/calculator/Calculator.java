package org.elsys.ip.calculator;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Memory memory = new Memory();
        CommandFactory commandFactory = new CommandFactory(memory);
        CommandExecutor commandExecutor = new CommandExecutor(commandFactory);
        while(true) {
            String line = scanner.nextLine();
            String[] lineSplit = line.split(" ");
            try {
                String result = commandExecutor.execute(
                        lineSplit[0],
                        Arrays.stream(lineSplit).skip(1).collect(Collectors.toList())
                );
                System.out.println(result);
            } catch (ExitException exit) {
                return;
            }
        }
    }
}
