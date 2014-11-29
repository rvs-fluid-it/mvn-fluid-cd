package be.fluid.mvn.cd.x.facets;

public class PolishException extends RuntimeException {
    public PolishException(String message) {
        super(message);
    }

    public PolishException(String message, Throwable cause) {
        super(message, cause);
    }
}
