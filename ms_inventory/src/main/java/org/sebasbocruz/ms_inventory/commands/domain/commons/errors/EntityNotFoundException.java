package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;


@Getter
@Setter
public class EntityNotFoundException extends DomainException {

    private String resourceType;
    private String identifier;

    public EntityNotFoundException(String resourceType, String identifier){
        super(String.format("%s with identifier (ID) '%s' was not found",resourceType, identifier),"Inventory");
        this.resourceType = resourceType;
        this.identifier = identifier;
    }


    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String code() {
        return "RESORCE_NOT_FOUND";
    }

    @Override
    public Map<String, Object> extraAttributes(){
        return Map.of("ResourceType",this.resourceType, "identifier",this.resourceType);
    }
}
