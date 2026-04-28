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
        Map<String, Object> errorMap = super.getErrorAttributes(serverRequest,options);
        Throwable rawError = getError(serverRequest);

        if(rawError instanceof DomainException ex){
            errorMap.put("status", ex.status().value());
            errorMap.put("error", ex.status().getReasonPhrase());
            errorMap.put("code", ex.code());
            errorMap.put("domain", ex.getDomain());
            errorMap.put("timestamp", ex.getTimestamp().toString());

            // each subclass can contribute its own fields
            errorMap.putAll(ex.extraAttributes());
        }

        errorMap.put("message",rawError.getMessage());
        errorMap.put("path",serverRequest.path());
        errorMap.put("method",serverRequest.method());

        return errorMap;

    }
}
