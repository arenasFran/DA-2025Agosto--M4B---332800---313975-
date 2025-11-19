package obligatorio.obli.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import obligatorio.obli.exceptions.BaseHttpException;
import obligatorio.obli.exceptions.PropietarioException;
import obligatorio.obli.exceptions.BonificacionException;
import obligatorio.obli.exceptions.PuestoException;
import obligatorio.obli.exceptions.TransitoException;
import obligatorio.obli.exceptions.AdministradorException;
import obligatorio.obli.exceptions.LoginException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ErrorResponse> handleBaseHttpException(BaseHttpException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PropietarioException.class)
    public ResponseEntity<ErrorResponse> handlePropietarioException(PropietarioException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BonificacionException.class)
    public ResponseEntity<ErrorResponse> handleBonificacionException(BonificacionException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PuestoException.class)
    public ResponseEntity<ErrorResponse> handlePuestoException(PuestoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TransitoException.class)
    public ResponseEntity<ErrorResponse> handleTransitoException(TransitoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AdministradorException.class)
    public ResponseEntity<ErrorResponse> handleAdministradorException(AdministradorException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(LoginException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleServletRequestBindingException(ServletRequestBindingException ex) {
        if (ex.getMessage().contains(LoginController.SESSION_ADMIN_COOKIE)) {
            var error = new AdministradorException("Sesión de administrador no encontrada. Debe iniciar sesión.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(error.getMessage()));
        }

        var error = new BaseHttpException(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(error.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
