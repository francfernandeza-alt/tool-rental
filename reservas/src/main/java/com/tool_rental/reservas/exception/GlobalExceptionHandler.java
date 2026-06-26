package com.tool_rental.reservas.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tool_rental.reservas.dto.ErrorDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> validaciones(MethodArgumentNotValidException e) {
        String detalle = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setMensaje("Datos de entrada invalidos.");
        errorDTO.setDetalle(detalle);
        errorDTO.setCodigoEstado(HttpStatus.BAD_REQUEST.value());
        errorDTO.setFecha(LocalDateTime.now());

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> erroresDeNegocio(RuntimeException e) {
        HttpStatus estado = obtenerEstado(e.getMessage());

        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setMensaje("No se pudo completar la operacion.");
        errorDTO.setDetalle(e.getMessage());
        errorDTO.setCodigoEstado(estado.value());
        errorDTO.setFecha(LocalDateTime.now());

        return new ResponseEntity<>(errorDTO, estado);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> errorGlobal(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setMensaje("Error interno en el microservicio de reservas");
        errorDTO.setDetalle("Ocurrio un problema tecnico durante la operacion");
        errorDTO.setCodigoEstado(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setFecha(LocalDateTime.now());

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus obtenerEstado(String mensaje) {
        if (mensaje == null) {
            return HttpStatus.BAD_REQUEST;
        }

        String mensajeMinuscula = mensaje.toLowerCase();

        if (mensajeMinuscula.contains("no se encontro")
                || mensajeMinuscula.contains("no se encontrÃ³")
                || mensajeMinuscula.contains("no existe")) {
            return HttpStatus.NOT_FOUND;
        }

        return HttpStatus.BAD_REQUEST;
    }
}


