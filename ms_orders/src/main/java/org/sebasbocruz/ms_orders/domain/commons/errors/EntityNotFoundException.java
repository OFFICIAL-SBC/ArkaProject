package org.sebasbocruz.ms_orders.domain.commons.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class EntityNotFoundException extends DomainException{

    private final String resourceType; // "User", "Order", "Product"
    private final String identifier; // The ID of key that was search

    public EntityNotFoundException(String resourceType, String identifier, String domain){
        super(String.format("%s with identifier '%s' was not found", resourceType, identifier), domain);
        this.resourceType = resourceType;
        this.identifier = identifier;
    }

    @Override
    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String code() {return "RESORCE_NOT_FOUND";}

    @Override
    public Map<String, Object> extraAttributes() {
        return Map.of("resourceType", resourceType, "identifier", identifier);
    }
}
