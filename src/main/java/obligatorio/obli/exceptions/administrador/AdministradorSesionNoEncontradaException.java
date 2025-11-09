package obligatorio.obli.exceptions.administrador;

import obligatorio.obli.exceptions.BaseHttpException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an admin session is required but not found.
 * This typically happens when trying to access admin endpoints without being
 * logged in.
 */
public class AdministradorSesionNoEncontradaException extends BaseHttpException {
  public AdministradorSesionNoEncontradaException() {
    super("Sesión de administrador no encontrada. Debe iniciar sesión.",
        "SESION_ADMIN_NO_ENCONTRADA",
        HttpStatus.UNAUTHORIZED);
  }

  public AdministradorSesionNoEncontradaException(String message) {
    super(message, "SESION_ADMIN_NO_ENCONTRADA", HttpStatus.UNAUTHORIZED);
  }

  public AdministradorSesionNoEncontradaException(String message, Throwable cause) {
    super(message, "SESION_ADMIN_NO_ENCONTRADA", HttpStatus.UNAUTHORIZED, cause);
  }
}
