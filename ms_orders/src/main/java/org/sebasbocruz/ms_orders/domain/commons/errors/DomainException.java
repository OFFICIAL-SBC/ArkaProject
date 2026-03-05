package org.sebasbocruz.ms_orders.domain.commons.errors;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {

    public abstract HttpStatus status();

}
