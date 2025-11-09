package obligatorio.obli.exceptions.puesto;

import org.springframework.http.HttpStatus;

import obligatorio.obli.exceptions.BaseHttpException;

public class PuestoNoEncontradoException extends BaseHttpException {

  public PuestoNoEncontradoException(String nombre) {
    super(String.format("No se encontró ningún puesto con el nombre: %s.", nombre), "PUESTO_NO_ENCONTRADO",
        HttpStatus.NOT_FOUND);
  }

  public PuestoNoEncontradoException(String nombre, Throwable cause) {
    super(String.format("No se encontró ningún puesto con el nombre: %s.", nombre), "PUESTO_NO_ENCONTRADO",
        HttpStatus.NOT_FOUND, cause);
  }
}
