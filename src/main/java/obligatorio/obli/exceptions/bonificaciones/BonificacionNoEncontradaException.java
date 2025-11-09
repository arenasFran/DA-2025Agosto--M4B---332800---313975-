package obligatorio.obli.exceptions.bonificaciones;

import org.springframework.http.HttpStatus;

import obligatorio.obli.exceptions.BaseHttpException;

public class BonificacionNoEncontradaException extends BaseHttpException {
  public BonificacionNoEncontradaException(String nombre) {
    super(String.format("No se encontró ninguna bonificación con el nombre: %s.", nombre), "BONIFICACION_NO_ENCONTRADA",
        HttpStatus.NOT_FOUND);
  }

  public BonificacionNoEncontradaException(String nombre, Throwable cause) {
    super(String.format("No se encontró ninguna bonificación con el nombre: %s.", nombre), "BONIFICACION_NO_ENCONTRADA",
        HttpStatus.NOT_FOUND, cause);
  }
}
