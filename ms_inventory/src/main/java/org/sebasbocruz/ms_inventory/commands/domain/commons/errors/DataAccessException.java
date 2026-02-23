package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DataAccessException extends DomainException{

    private String entityMessage;

    // TODO: I need to add the actual message of the error and its details in ht ems response
    public DataAccessException(String errorMessage, Throwable ex){
        this.entityMessage = errorMessage;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
