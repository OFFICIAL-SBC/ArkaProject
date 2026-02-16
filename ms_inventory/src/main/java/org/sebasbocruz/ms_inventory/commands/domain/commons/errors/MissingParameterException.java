package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import org.springframework.http.HttpStatus;

public class MissingParameterException extends DomainException{
    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
