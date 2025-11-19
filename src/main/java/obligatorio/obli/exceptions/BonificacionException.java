package obligatorio.obli.exceptions;

/**
 * Generic exception for all Bonificacion-related errors.
 * Uses custom messages to differentiate between different error scenarios.
 */
public class BonificacionException extends BaseHttpException {
  public BonificacionException(String message) {
    super(message);
  }

  public BonificacionException(String message, Throwable cause) {
    super(message, cause);
  }
}
