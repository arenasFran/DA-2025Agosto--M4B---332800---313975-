package obligatorio.obli.exceptions.propietario.estados;

import org.springframework.http.HttpStatus;

import obligatorio.obli.exceptions.BaseHttpException;
import obligatorio.obli.models.Estados.Estado;

public class EstadoProhibidoRecibirBonificacionException extends BaseHttpException {
  public EstadoProhibidoRecibirBonificacionException(Estado estado) {
    super(String.format("Prohibido asignar bonificación a propietario en estado '%s'.", estado.getNombre()),
        "ESTADO_PROHIBIDO_RECIBIR_BONIFICACION",
        HttpStatus.FORBIDDEN);
  }
}
