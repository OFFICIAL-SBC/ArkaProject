package org.sebasbocruz.ms_orders.domain.commons.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Getter
public abstract class DomainException extends RuntimeException {

    public abstract HttpStatus status();
    public abstract String code();

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String domain;

    protected DomainException(String message, String domain){
        super(message);
        this.domain = domain;
    }

    protected DomainException(String message, String domain, Throwable rawCause){
        super(message, rawCause);
        this.domain = domain;
    }

    public Map<String, Object> extraAttributes(){
        return Collections.emptyMap();
    }

}
