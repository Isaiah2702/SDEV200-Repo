public class BinaryFormatException extends Exception {

    public BinaryFormatException() {
        super("The string is not a valid binary string.");
    }

    public BinaryFormatException(String message) {
        super(message);
    }
}
