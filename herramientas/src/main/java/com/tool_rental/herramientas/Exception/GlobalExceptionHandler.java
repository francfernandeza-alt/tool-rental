package com.tool_rental.herramientas.Exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tool_rental.herramientas.DTO.ErrorDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity <ErrorDTO> notFound (RuntimeException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensaje("Recurso no encontrado.");
        errorDTO.setDetalle(e.getMessage());
        errorDTO.setCodigoEstado(HttpStatus.NOT_FOUND.value());
        errorDTO.setFecha(LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity <ErrorDTO> validaciones (MethodArgumentNotValidException e) {
        String detalle = e.getBindingResult().getFieldErrors().stream().map(err -> err.getField() + ". " + err.getDefaultMessage()).collect(Collectors.joining(", "));
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensaje("Datos de entrada inválidos");
        errorDTO.setDetalle(detalle);
        errorDTO.setCodigoEstado(HttpStatus.BAD_REQUEST.value());
        errorDTO.setFecha(LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> errorGlobal(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensaje("Error interno en el servidor");
        errorDTO.setDetalle("Ocurrió un problema técnico, contacte al administrador.");
        errorDTO.setCodigoEstado(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setFecha(LocalDateTime.now());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
