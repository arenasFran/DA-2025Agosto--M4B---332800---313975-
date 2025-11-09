package obligatorio.obli.exceptions.propietario;

import org.springframework.http.HttpStatus;
import obligatorio.obli.exceptions.BaseHttpException;

public class PropietarioNoEncontradoException extends BaseHttpException {
  public PropietarioNoEncontradoException(String ci) {
    super("No se encontró ningún propietario con la cédula: " + ci, "PROPIETARIO_NO_ENCONTRADO", HttpStatus.NOT_FOUND);
  }

  public PropietarioNoEncontradoException(String message, Throwable cause) {
    super(message, "PROPIETARIO_NO_ENCONTRADO", HttpStatus.NOT_FOUND, cause);
  }
}
