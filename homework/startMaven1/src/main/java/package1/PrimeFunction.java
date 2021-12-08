package package1;

import java.math.BigInteger;
import java.util.List;

public class PrimeFunction implements Function {

    private boolean isPrime(int a) {
        if (a <= 1) return false;

        for (int i = 2; i < a; i++)
            if (a % i == 0) return false;

        return true;
    }

    private void checkIfDecimal(String str) {
        try {
            System.out.println(Double.parseDouble(str) + " is not an integer");
        } catch (NumberFormatException e) {
            System.out.println(str + " is not a number");
        }
    }

    @Override
    public void execute(List<String> args) {

        for (String number : args) {
            try {
                Integer.parseInt(number);
            } catch (Exception ex) {
                try {
                    System.out.println(new BigInteger(number) + " is out of bound");
                }catch (Exception exception) {
                    checkIfDecimal(number);
                }
                continue;
            }

            if (isPrime(Integer.parseInt(number)))
                System.out.println(number + " is a prime");
            else
                System.out.println(number + " is not a prime");

        }
    }
}
