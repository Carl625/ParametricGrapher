package FunctionGrapher;

public class FunctionFormatException extends Exception {

    public FunctionFormatException(String message, Throwable origin) {

        super("Illegal Function Format: " + message, origin);
    }
}
