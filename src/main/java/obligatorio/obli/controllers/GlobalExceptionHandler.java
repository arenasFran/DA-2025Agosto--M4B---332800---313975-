package obligatorio.obli.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import obligatorio.obli.exceptions.BaseHttpException;
import obligatorio.obli.exceptions.administrador.AdministradorSesionNoEncontradaException;
import obligatorio.obli.exceptions.login.SistemaLoginException;
import obligatorio.obli.exceptions.propietario.PropietarioNoEncontradoException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ErrorResponse> handleBaseHttpException(BaseHttpException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(ex.getStatusCode().value()).body(error);
    }

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

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<BaseHttpException> handleServletRequestBindingException(ServletRequestBindingException ex) {
        if (ex.getMessage().contains(LoginController.SESSION_ADMIN_COOKIE)) {
            var error = new AdministradorSesionNoEncontradaException();
            return ResponseEntity.status(error.getStatusCode().value()).body(error);
        }

        var error = new BaseHttpException(ex.getMessage(), "REQUEST_BINDING_ERROR", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(error.getStatusCode().value()).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
