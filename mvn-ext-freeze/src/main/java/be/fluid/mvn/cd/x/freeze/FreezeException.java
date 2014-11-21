package be.fluid.mvn.cd.x.freeze;

public class FreezeException extends RuntimeException {
    public FreezeException(String message) {
        super(message);
    }

    public FreezeException(String message, Throwable cause) {
        super(message, cause);
    }
}
