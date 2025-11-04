package obligatorio.obli.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import obligatorio.obli.exceptions.PropietarioNoEncontradoException;
import obligatorio.obli.exceptions.SistemaLoginException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PropietarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePropietarioNoEncontrado(PropietarioNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "PROPIETARIO_NO_ENCONTRADO");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SistemaLoginException.class)
    public ResponseEntity<ErrorResponse> handleSistemaLoginException(SistemaLoginException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "LOGIN_DENEGADO");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
