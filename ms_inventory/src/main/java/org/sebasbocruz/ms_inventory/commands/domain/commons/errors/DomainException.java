package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Getter
public abstract class DomainException extends RuntimeException {

    public abstract HttpStatus status();
    public abstract String code();

    private LocalDateTime timestamp = LocalDateTime.now(); // ! This variable is assign at OBJECT INSTANTIATION TIME.
    private final String domain;

    protected DomainException(String message, String domain){
        // !This method is used when my logic detects the problem
        // ! Do not underlying error triggers it
        // ? message -> Human-readable description of what went wrong this is the message that describe the problem

        super(message); // ! -> This called the constructor RuntimeException(String message)
                        // ! -> Pay attention here since I'm only calling super(message)
                        // ! -> I am not assigning it to anything else, BUT I WILL BE ABLE TO GET THE MESSAGE
                        //  * -> calling the RuntimeException.getMessage() method
        this.domain = domain;
    }


    protected DomainException(String message, String domain, Throwable rawError){
        // ! I use this when a lower-level exception triggered mine.
        // * Something outsidE my domain EXPLODED (Database, HTTP CLIENT, CASTING ERROR, TIMEOUT)
        // * So with this I am **WRAPPING IT IN A MEANINGFUL DOMAIN EXCEPTION** instead of just
        // * leaking the raw technical error to the client

        // ? String message -> My domain-level description of the failure
        // ? String domain -> The bounded context that owns this error
        // ? Throwable rawCause -> The original low-level exception that triggered this.

        super(message, rawError); // ! -> This call RuntimeException(String message, Throwable error)
                                  // ! -> This store both. This is critical. It PRESERVES THE FULL STACK TRACE OF THE ORIGINAL ERROR

        this.domain = domain;
    }


    public Map<String, Object> extraAttributes(){
        // ! This function is OVERWRITE IT on each subclass of the class DomainException

        return Collections.emptyMap();
    }


}
