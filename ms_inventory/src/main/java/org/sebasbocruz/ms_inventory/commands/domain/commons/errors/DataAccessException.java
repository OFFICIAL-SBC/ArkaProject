package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class DataAccessException extends DomainException{

    public DataAccessException(){
        super("","");
    }


    @Override
    public HttpStatus status() {
        return null;
    }

    @Override
    public String code() {
        return "";
    }
}
