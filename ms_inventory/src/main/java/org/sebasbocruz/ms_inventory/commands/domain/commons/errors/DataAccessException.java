package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DataAccessException extends DomainException{

    private String entityMessage;

    public DataAccessException(String errorMessage){
        this.entityMessage = errorMessage;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
