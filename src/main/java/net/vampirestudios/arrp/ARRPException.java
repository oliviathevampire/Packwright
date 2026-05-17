package net.vampirestudios.arrp;

/**
 * Base exception for ARRP runtime errors. Thrown when resource serialization,
 * IO, or pack operations fail in a way that cannot be recovered from.
 */
public class ARRPException extends RuntimeException {
    public ARRPException(String message) {
        super(message);
    }

    public ARRPException(String message, Throwable cause) {
        super(message, cause);
    }

    public ARRPException(Throwable cause) {
        super(cause);
    }
}
