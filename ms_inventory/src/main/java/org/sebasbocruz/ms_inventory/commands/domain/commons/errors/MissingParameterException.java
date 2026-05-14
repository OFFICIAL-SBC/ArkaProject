package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class MissingParameterException extends DomainException{

    private final String param;

    public MissingParameterException(String param){
        super(String.format("Missing query param is necessary"),"ms_inventory");
        this.param = param;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String code() {
        return "REQUIRED_PARAMETER_MISSING";
    }

    @Override
    public Map<String, Object> extraAttributes(){
        return Map.of("missing_param",param);
    }
}
