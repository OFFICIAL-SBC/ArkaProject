package org.sebasbocruz.ms_orders.domain.commons.errors;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter @Setter
public class DataAccessException extends DomainException{

    private final String operation;

    public DataAccessException(String operation, String domain, Throwable cause){
        super(String.format("Data access failed during %s operation",operation),domain,cause);
        this.operation =  operation;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    @Override
    public String code(){ return "DATA_ACCESS_FAILURE";}


    @Override
    public Map<String, Object> extraAttributes(){
        return Map.of("operation",operation);
    }
}
