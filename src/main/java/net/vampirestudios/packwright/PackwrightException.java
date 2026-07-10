package net.vampirestudios.packwright;

/**
 * Base exception for Packwright runtime errors. Thrown when resource serialization,
 * IO, or pack operations fail in a way that cannot be recovered from.
 */
public class PackwrightException extends RuntimeException {
    public PackwrightException(String message) {
        super(message);
    }

    public PackwrightException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackwrightException(Throwable cause) {
        super(cause);
    }
}
