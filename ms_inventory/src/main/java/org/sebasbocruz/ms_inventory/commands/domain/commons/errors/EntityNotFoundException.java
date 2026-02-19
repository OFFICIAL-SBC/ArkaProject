package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class EntityNotFoundException extends DomainException {

    private String entityMessage;

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
