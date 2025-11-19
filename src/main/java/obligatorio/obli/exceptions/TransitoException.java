package obligatorio.obli.exceptions;

/**
 * Generic exception for all Transito-related errors.
 * Uses custom messages to differentiate between different error scenarios.
 */
public class TransitoException extends BaseHttpException {
  public TransitoException(String message) {
    super(message);
  }

  public TransitoException(String message, Throwable cause) {
    super(message, cause);
  }
}

