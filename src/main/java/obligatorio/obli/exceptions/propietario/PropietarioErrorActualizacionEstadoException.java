package obligatorio.obli.exceptions.propietario;

import org.springframework.http.HttpStatus;
import obligatorio.obli.exceptions.BaseHttpException;

public class PropietarioErrorActualizacionEstadoException extends BaseHttpException {
  public PropietarioErrorActualizacionEstadoException(String message) {
    super(message, "PROPIETARIO_ERROR_ACTUALIZACION_ESTADO", HttpStatus.BAD_REQUEST);
  }

  public PropietarioErrorActualizacionEstadoException(String message, Throwable cause) {
    super(message, "PROPIETARIO_ERROR_ACTUALIZACION_ESTADO", HttpStatus.BAD_REQUEST, cause);
  }
}
