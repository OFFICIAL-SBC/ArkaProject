package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InsufficientStockException extends DomainException{

    private String entityMessage;

    public InsufficientStockException(String entityMessage){
        this.entityMessage = entityMessage;
    }
    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
