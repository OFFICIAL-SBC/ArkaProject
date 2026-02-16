package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import org.springframework.http.HttpStatus;

public class DataAccessException extends DomainException{
    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
