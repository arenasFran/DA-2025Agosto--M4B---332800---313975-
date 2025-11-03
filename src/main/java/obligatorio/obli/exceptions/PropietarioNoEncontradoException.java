package obligatorio.obli.exceptions;

public class PropietarioNoEncontradoException extends RuntimeException {

    public PropietarioNoEncontradoException(String ci) {
        super("No se encontró ningún propietario con la cédula: " + ci);
    }

    public PropietarioNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
