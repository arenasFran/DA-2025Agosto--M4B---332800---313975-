package obligatorio.obli.exceptions;

/**
 * Generic exception for all Login-related errors.
 * Uses custom messages to differentiate between different error scenarios.
 */
public class LoginException extends BaseHttpException {
  public LoginException(String message) {
    super(message);
  }

  public LoginException(String message, Throwable cause) {
    super(message, cause);
  }
}

