package org.elsys.ip.calculator;

import java.util.Arrays;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandFactory commandFactory = new CommandFactory();
        CommandExecutor commandExecutor = new CommandExecutor(commandFactory);
        while(true) {
            String line = scanner.nextLine();
            String[] lineSplit = line.split(" ");
            String result = commandExecutor.execute(
                    lineSplit[0],
                    Arrays.stream(lineSplit).skip(1).toList()
            );
            System.out.println(result);
        }
    }
}
