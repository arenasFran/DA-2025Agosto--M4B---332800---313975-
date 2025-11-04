package ort.da.agenda;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ort.da.agenda.modelo.AgendaException;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(AgendaException.class)
    public ResponseEntity<String> manejarException(AgendaException ex) {
        
       return ResponseEntity.status(299).body(ex.getMessage());
    }
}
