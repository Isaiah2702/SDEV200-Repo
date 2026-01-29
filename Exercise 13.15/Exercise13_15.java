import java.math.BigInteger;
import java.util.Scanner;

public class Exercise13_15 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter rational r1 with numerator and denominator separated by a space: ");
        BigInteger n1 = input.nextBigInteger();
        BigInteger d1 = input.nextBigInteger();

        System.out.print("Enter rational r2 with numerator and denominator separated by a space: ");
        BigInteger n2 = input.nextBigInteger();
        BigInteger d2 = input.nextBigInteger();

        Rational r1 = new Rational(n1, d1);
        Rational r2 = new Rational(n2, d2);

        System.out.println(r1 + " + " + r2 + " = " + r1.add(r2));
        System.out.println(r1 + " - " + r2 + " = " + r1.subtract(r2));
        System.out.println(r1 + " * " + r2 + " = " + r1.multiply(r2));
        System.out.println(r1 + " / " + r2 + " = " + r1.divide(r2));
        System.out.println(r2 + " is " + r2.doubleValue());
    }
}
