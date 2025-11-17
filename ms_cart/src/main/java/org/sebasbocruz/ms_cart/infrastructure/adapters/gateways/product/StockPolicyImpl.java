package org.sebasbocruz.ms_cart.infrastructure.adapters.gateways.product;

import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.StockPolicy;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.infrastructure.adapters.gateways.product.dtos.StockPolicyDTO;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPolicyImpl implements StockPolicy {

    private final RestTemplate restTemplate;
    private String baseUrl = "http://localhost:8082";
    private Logger logger = org.slf4j.LoggerFactory.getLogger(StockPolicyImpl.class);

    public StockPolicyImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean isAvailable(ProductId productId, int quantity) {

        StockPolicyDTO response = restTemplate.getForObject(
                baseUrl + "/api/v1/inventory/available-policy?productId={productId}",
                StockPolicyDTO.class,
                productId.value()
        );

        if (response == null) {
            throw new IllegalStateException("No response from inventory service for product ID " + productId.value());
        }

        logger.warn("StockPolicyImpl - Product ID: {}, Available Stock: {}, Requested Quantity: {}",
                productId.value(), response.getAvailableStock(), quantity);

        return response.getAvailableStock() >= quantity;
    }

}
