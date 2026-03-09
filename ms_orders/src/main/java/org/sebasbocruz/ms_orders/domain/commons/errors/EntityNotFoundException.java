package org.sebasbocruz.ms_orders.domain.commons.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class EntityNotFoundException extends DomainException{

    private String message;

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
