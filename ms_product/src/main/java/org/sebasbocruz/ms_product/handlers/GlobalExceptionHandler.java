package org.sebasbocruz.ms_product.handlers;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // DTO para respuestas de error estructuradas
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

    // ============================
    // ERRORES ESPECÍFICOS DEL CONTROLADOR
    // ============================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("Invalid argument in {}: {}", request.getRequestURI(), ex.getMessage());

        // Detectar si es categoría o marca basándose en la URL
        String errorType = "INVALID_PARAMETER";
        String headerName = "X-Parameter";

        if (request.getRequestURI().contains("/category")) {
            errorType = "INVALID_CATEGORY";
            headerName = "X-Category";
        } else if (request.getRequestURI().contains("/brand")) {
            errorType = "INVALID_BRAND";
            headerName = "X-Brand";
        }

        Map<String, Object> details = new HashMap<>();
        details.put("parameter", request.getParameter("category") != null ?
                request.getParameter("category") : request.getParameter("brand"));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(errorType)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .details(details)
                .traceId(generateTraceId())
                .build();

        return ResponseEntity
                .badRequest()
                .header("X-Error-Type", errorType)
                .header(headerName, (String) details.get("parameter"))
                .header("X-Error-Message", ex.getMessage())
                .header("X-Trace-ID", errorResponse.getTraceId())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(errorResponse);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(
            DataAccessException ex, HttpServletRequest request) {

        log.error("Database error in {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        // Extraer parámetros de la request para contexto
        String category = request.getParameter("category");
        String brand = request.getParameter("brand");

        Map<String, Object> details = new HashMap<>();
        if (category != null) {
            details.put("category", category);
        }
        if (brand != null) {
            details.put("brand", brand);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("DATABASE_ERROR")
                .message("Error interno del servidor. Contacte al administrador si persiste.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .details(details)
                .traceId(generateTraceId())
                .build();

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Error-Type", "DATABASE_ERROR")
                .header("X-Trace-ID", errorResponse.getTraceId())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        // Agregar headers específicos basándose en el contexto
        if (category != null) {
            responseBuilder.header("X-Category", category);
        }
        if (brand != null) {
            responseBuilder.header("X-Brand", brand);
        }

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
                .traceId(generateTraceId())
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

        // Extraer parámetros de la request para contexto
        String category = request.getParameter("category");
        String brand = request.getParameter("brand");

        Map<String, Object> details = new HashMap<>();
        details.put("exceptionType", ex.getClass().getSimpleName());
        if (category != null) {
            details.put("category", category);
        }
        if (brand != null) {
            details.put("brand", brand);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("INTERNAL_ERROR")
                .message("Error interno del servidor. Contacte al administrador si persiste.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .details(details)
                .traceId(generateTraceId())
                .build();

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Error-Type", "INTERNAL_ERROR")
                .header("X-Trace-ID", errorResponse.getTraceId())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        // Agregar headers específicos basándose en el contexto
        if (category != null) {
            responseBuilder.header("X-Category", category);
        }
        if (brand != null) {
            responseBuilder.header("X-Brand", brand);
        }

        return responseBuilder.body(errorResponse);
    }

    // ============================
    // UTILIDADES
    // ============================

    private String generateTraceId() {
        return "TRACE-" + System.currentTimeMillis() + "-" +
                Integer.toHexString((int)(Math.random() * 0x10000));
    }
}