import java.util.Scanner;

public class Exercise06_31 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a credit card number as a long integer: ");
        long number = input.nextLong();

        if (isValid(number)) {
            System.out.println(number + " is valid");
        } else {
            System.out.println(number + " is invalid");
        }

        input.close();
    }

    /** Return true if the card number is valid */
    public static boolean isValid(long number) {
        int size = getSize(number);

        // Check length
        if (size < 13 || size > 16) {
            return false;
        }

        // Check prefix
        boolean prefixOk =
                prefixMatched(number, 4) ||
                prefixMatched(number, 5) ||
                prefixMatched(number, 37) ||
                prefixMatched(number, 6);

        if (!prefixOk) {
            return false;
        }

        // Luhn check
        int total = sumOfDoubleEvenPlace(number) + sumOfOddPlace(number);
        return total % 10 == 0;
    }

    /** Get the result from Step 2 */
    public static int sumOfDoubleEvenPlace(long number) {
        int sum = 0;
        String s = Long.toString(number);

        for (int i = s.length() - 2; i >= 0; i -= 2) {
            int digit = s.charAt(i) - '0';
            sum += getDigit(digit * 2);
        }

        return sum;
    }

    /** Return this number if it is a single digit, otherwise return the sum of the two digits */
    public static int getDigit(int number) {
        if (number < 10) {
            return number;
        }
        return (number / 10) + (number % 10);
    }

    /** Return sum of odd-place digits in number */
    public static int sumOfOddPlace(long number) {
        int sum = 0;
        String s = Long.toString(number);

        for (int i = s.length() - 1; i >= 0; i -= 2) {
            sum += s.charAt(i) - '0';
        }

        return sum;
    }

    /** Return true if the number d is a prefix for number */
    public static boolean prefixMatched(long number, int d) {
        int k = getSize(d);
        return getPrefix(number, k) == d;
    }

    /** Return the number of digits in d */
    public static int getSize(long d) {
        return Long.toString(d).length();
    }

    /** Return the first k number of digits from number */
    public static long getPrefix(long number, int k) {
        String s = Long.toString(number);
        if (s.length() <= k) {
            return number;
        }
        return Long.parseLong(s.substring(0, k));
    }
}
