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

        // ! I use this when my own logic detects the problem.
        // ! No underlying EXCEPTION TRIGGERED IT
        // ? message A human-readable description of what went wrong

        super(message); // ! -> This call RuntimeException(String message) which store the message internally in the Throwable chain
        this.domain = domain;
    }

    protected DomainException(String message, String domain, Throwable rawCause){

        // ! I use this when a lower-level exception triggered mine. Something outside
        // ! my domain exploded (a database, HTTP client , a timeout)
        // ! so with this I am wrapping it in a meaningful domain exception instead of leaking the
        // ! raw technical error to the consumer

        // ? String message -> My domain-level description of the failure
        // ? String domain -> The bounded context that owns this error
        // ? Throwable rawCause -> The original low-level exception that triggered this.

        super(message, rawCause); // ! This call RuntimeException(String message Throwable rawCause)
                                 // ! Which stores both. this is critical, IT PRESERVES THE FULL STACK TRACE OF THE ORIGINAL EXCEPTION
        this.domain = domain;
    }

    public Map<String, Object> extraAttributes(){
        return Collections.emptyMap();
    }

}
