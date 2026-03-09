package org.sebasbocruz.ms_orders.domain.commons.errors;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public class DataAccessException extends DomainException{

    private String message;
    private Throwable ex;

    public DataAccessException(String message,Throwable ex ){

        this.ex = ex;
        this.message = message;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
