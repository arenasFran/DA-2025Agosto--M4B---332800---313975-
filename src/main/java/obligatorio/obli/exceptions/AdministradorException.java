package obligatorio.obli.exceptions;

/**
 * Generic exception for all Administrador-related errors.
 * Uses custom messages to differentiate between different error scenarios.
 */
public class AdministradorException extends BaseHttpException {
  public AdministradorException(String message) {
    super(message);
  }

  public AdministradorException(String message, Throwable cause) {
    super(message, cause);
  }
}

