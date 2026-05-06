package org.sebasbocruz.ms_orders.domain.commons.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class InvalidStateTransitionException extends DomainException{

    private final String resourceType;
    private final String currentState;
    private final String attemptedState;

    public InvalidStateTransitionException(String resourceType, String currentState, String attemptedState, String domain){
        super(String.format("Cannot transition %s from '%s' to '%s'", resourceType,currentState,attemptedState),domain);
        this.resourceType = resourceType;
        this.attemptedState = attemptedState;
        this.currentState = currentState;
    }

    @Override
    public HttpStatus status() {
        return  HttpStatus.CONFLICT;
    }

    @Override
    public String code() {
        return "INVALID_STATE_TRANSITION";
    }

    @Override
    public Map<String, Object> extraAttributes(){
        return Map.of("resourceType",resourceType,"currentState",currentState, "attemptedState",attemptedState);
    }

}
