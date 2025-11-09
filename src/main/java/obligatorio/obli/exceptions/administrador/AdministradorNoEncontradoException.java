package obligatorio.obli.exceptions.administrador;

import obligatorio.obli.exceptions.BaseHttpException;
import org.springframework.http.HttpStatus;

public class AdministradorNoEncontradoException extends BaseHttpException {
  public AdministradorNoEncontradoException(String ci) {
    super("No se encontró ningún administrador con la cédula: " + ci, "ADMINISTRADOR_NO_ENCONTRADO",
        HttpStatus.NOT_FOUND);
  }

  public AdministradorNoEncontradoException(String message, Throwable cause) {
    super(message, "ADMINISTRADOR_NO_ENCONTRADO", HttpStatus.NOT_FOUND, cause);
  }
}
