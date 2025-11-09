package obligatorio.obli.exceptions.login;

import org.springframework.http.HttpStatus;

import obligatorio.obli.exceptions.BaseHttpException;

public class LoginUnauthenticatedException extends BaseHttpException {
  public LoginUnauthenticatedException() {
    super("Debe iniciar sesión como administrador", "LOGIN_UNAUTHENTICATED", HttpStatus.UNAUTHORIZED);
  }

  public LoginUnauthenticatedException(String message) {
    super(message, "LOGIN_UNAUTHENTICATED", HttpStatus.UNAUTHORIZED);
  }

  public LoginUnauthenticatedException(String message, Throwable cause) {
    super(message, "LOGIN_UNAUTHENTICATED", HttpStatus.UNAUTHORIZED, cause);
  }
}
