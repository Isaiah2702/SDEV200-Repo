import java.math.BigInteger;

public class Rational extends Number implements Comparable<Rational> {

    private BigInteger numerator = BigInteger.ZERO;
    private BigInteger denominator = BigInteger.ONE;

    /** Default constructor */
    public Rational() {
        this(BigInteger.ZERO, BigInteger.ONE);
    }

    /** Constructor with BigInteger arguments */
    public Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Denominator cannot be zero");
        }

        // Normalize sign
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        BigInteger gcd = numerator.gcd(denominator);
        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);
    }

    /** Constructor with long arguments */
    public Rational(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    /** Addition */
    public Rational add(Rational other) {
        BigInteger n = numerator.multiply(other.denominator)
                .add(other.numerator.multiply(denominator));
        BigInteger d = denominator.multiply(other.denominator);
        return new Rational(n, d);
    }

    /** Subtraction */
    public Rational subtract(Rational other) {
        BigInteger n = numerator.multiply(other.denominator)
                .subtract(other.numerator.multiply(denominator));
        BigInteger d = denominator.multiply(other.denominator);
        return new Rational(n, d);
    }

    /** Multiplication */
    public Rational multiply(Rational other) {
        BigInteger n = numerator.multiply(other.numerator);
        BigInteger d = denominator.multiply(other.denominator);
        return new Rational(n, d);
    }

    /** Division */
    public Rational divide(Rational other) {
        if (other.numerator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division by zero");
        }
        BigInteger n = numerator.multiply(other.denominator);
        BigInteger d = denominator.multiply(other.numerator);
        return new Rational(n, d);
    }

    /** Compare rationals */
    @Override
    public int compareTo(Rational o) {
        return numerator.multiply(o.denominator)
                .compareTo(o.numerator.multiply(denominator));
    }

    /** Convert to double */
    @Override
    public double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public int intValue() {
        return (int) doubleValue();
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    /** String representation */
    @Override
    public String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        }
        return numerator + "/" + denominator;
    }

    /** Equality */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rational)) return false;
        Rational r = (Rational) obj;
        return numerator.equals(r.numerator) &&
               denominator.equals(r.denominator);
    }
}
