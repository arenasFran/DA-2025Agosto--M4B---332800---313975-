package obligatorio.obli.exceptions;

/**
 * Generic exception for all Propietario-related errors.
 * Uses custom messages to differentiate between different error scenarios.
 */
public class PropietarioException extends BaseHttpException {
  public PropietarioException(String message) {
    super(message);
  }

  public PropietarioException(String message, Throwable cause) {
    super(message, cause);
  }
}
