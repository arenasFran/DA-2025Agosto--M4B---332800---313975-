package obligatorio.obli.exceptions.login;

import org.springframework.http.HttpStatus;

import obligatorio.obli.exceptions.BaseHttpException;

public class LoginCredencialesInvalidasException extends BaseHttpException {
  public LoginCredencialesInvalidasException() {
    super("Credenciales inválidas.", "LOGIN_CREDENCIALES_INVALIDAS", HttpStatus.UNAUTHORIZED);
  }

  public LoginCredencialesInvalidasException(String message) {
    super(message, "LOGIN_CREDENCIALES_INVALIDAS", HttpStatus.UNAUTHORIZED);
  }

  public LoginCredencialesInvalidasException(String message, Throwable cause) {
    super(message, "LOGIN_CREDENCIALES_INVALIDAS", HttpStatus.UNAUTHORIZED, cause);
  }
}
