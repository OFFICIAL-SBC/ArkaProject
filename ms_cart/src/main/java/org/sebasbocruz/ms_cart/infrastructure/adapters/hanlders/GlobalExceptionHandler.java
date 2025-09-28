package org.sebasbocruz.ms_cart.infrastructure.adapters.hanlders;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String error;
        private String message;
        private int status;
        private String path;
        private String method;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timestamp;

        private Map<String, Object> details;
        private String traceId;
    }



    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex, HttpServletRequest req) {

        log.error("Not entity found in {}: {}", req.getRequestURI(), ex.getMessage(), ex);



        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("ENTITY_NOT_FOUND")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(req.getRequestURI())
                .method(req.getMethod())
                .timestamp(LocalDateTime.now())
                .build();

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("X-Error-Type", "ENTITY_NOT_FOUND_ERROR")
                .header("X-Trace-ID", errorResponse.getTraceId())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE);


        return responseBuilder.body(errorResponse);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(
            DataAccessException ex, HttpServletRequest request) {

        log.error("Database error in {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("DATABASE_ERROR")
                .message("Error interno del servidor. Contacte al administrador si persiste.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .build();

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Error-Type", "DATABASE_ERROR")
                .header("X-Trace-ID", errorResponse.getTraceId())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE);


        return responseBuilder.body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        log.warn("Missing required parameter in {}: {}", request.getRequestURI(), ex.getParameterName());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("MISSING_PARAMETER")
                .message("Required parameter missing: " + ex.getParameterName())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .badRequest()
                .header("X-Error-Type", "MISSING_PARAMETER")
                .header("X-Missing-Parameter", ex.getParameterName())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        log.error("Unexpected error in {}: {}", request.getRequestURI(), ex.getMessage(), ex);



        Map<String, Object> details = new HashMap<>();
        details.put("exceptionType", ex.getClass().getSimpleName());


        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("INTERNAL_ERROR")
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Error-Type", "INTERNAL_ERROR")
                .header("X-Trace-ID", errorResponse.getTraceId())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE);


        return responseBuilder.body(errorResponse);
    }
}
