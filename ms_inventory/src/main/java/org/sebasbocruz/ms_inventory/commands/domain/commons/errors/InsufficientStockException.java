package org.sebasbocruz.ms_inventory.commands.domain.commons.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class InsufficientStockException extends DomainException{

    private final String product;
    private final int currentStock;
    private final int amountAsked;

    public InsufficientStockException(String product, int currentStock, int amountAsked ){
        super(String.format("Insufficient stock for product ID %s",product),"ms_inventory");
        this.product = product;
        this.currentStock = currentStock;
        this.amountAsked = amountAsked;
    }


    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String code() {
        return "NOT_ENOUGH_STOCK";
    }

    @Override
    public Map<String, Object> extraAttributes(){
        return Map.of("current_stock",currentStock, "amount_ordered",amountAsked);
    }

}
