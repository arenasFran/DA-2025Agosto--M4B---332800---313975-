package obligatorio.obli.exceptions.propietario.estados;

import org.springframework.http.HttpStatus;

import obligatorio.obli.exceptions.BaseHttpException;
import obligatorio.obli.models.Estados.Estado;

public class EstadoProhibidoIniciarSesionException extends BaseHttpException {
  public EstadoProhibidoIniciarSesionException(Estado estado) {
    super(String.format("Prohibido iniciar sesión en estado '%s'.", estado.getNombre()),
        "ESTADO_PROHIBIDO_INICIAR_SESION",
        HttpStatus.FORBIDDEN);
  }

  public EstadoProhibidoIniciarSesionException(String message, Throwable cause) {
    super(message, "ESTADO_PROHIBIDO_INICIAR_SESION", HttpStatus.FORBIDDEN, cause);
  }
}
