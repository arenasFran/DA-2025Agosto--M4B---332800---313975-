package obligatorio.obli.exceptions;

/**
 * Generic exception for all Puesto-related errors.
 * Uses custom messages to differentiate between different error scenarios.
 */
public class PuestoException extends BaseHttpException {
  public PuestoException(String message) {
    super(message);
  }

  public PuestoException(String message, Throwable cause) {
    super(message, cause);
  }
}

