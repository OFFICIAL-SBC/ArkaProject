package org.sebasbocruz.ms_orders.infrastructure.adapters.DTOS.OUT;

import org.sebasbocruz.ms_orders.domain.commons.errors.DomainException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorResponse extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest serverRequest, ErrorAttributeOptions options){
        Map<String, Object> errorMap = new HashMap<>();
        Throwable rawError = getError(serverRequest);

        if(rawError instanceof DomainException){
            errorMap.put("status",((DomainException) rawError).status().value());
            errorMap.put("error", ((DomainException) rawError).status().getReasonPhrase());
        }else{
            errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorMap.put("error", "INTERNAL_ERROR");
        }

        errorMap.put("message",rawError.getMessage());
        errorMap.put("path",serverRequest.path());
        errorMap.put("method",serverRequest.method());
        errorMap.put("timestamp", LocalDateTime.now());

        return errorMap;

    }
}
