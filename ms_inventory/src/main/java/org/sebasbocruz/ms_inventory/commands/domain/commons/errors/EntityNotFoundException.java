package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends DomainException {
    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
