package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor

public class EntityNotFoundException extends DomainException {

    private String entityMessage;

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
