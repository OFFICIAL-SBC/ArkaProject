package org.sebasbocruz.ms_cart.application.command;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.Aggregate.Cart;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.DomainServices.CartDomainService;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartId;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.ValueObjects.cart.CartLine;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.commands.CartCommandsGateway;
import org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out.DomainEventPublisher;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductId;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductName;
import org.sebasbocruz.ms_cart.domain.contexts.Product.ValueObjects.ProductPrice;
import org.sebasbocruz.ms_cart.domain.contexts.Product.entities.Product;
import org.sebasbocruz.ms_cart.domain.contexts.Product.gateway.CatalogQuery;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.dtos.LineDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AddItemToExistingCartUseCase {


    private final CartCommandsGateway cartCommandsGateway;
    private final CatalogQuery catalogQuery;
    private final CartDomainService domainService;
    private final DomainEventPublisher publisher;


    public LineDTO addItemToExistingCart(Long cart_id, LineDTO newLine){

        // --- Precondition checks
        Objects.requireNonNull(newLine, "newLine must not be null");

        if (newLine.getNumberOfProducts() <= 0) {
            throw new IllegalArgumentException("numberOfProducts must be > 0");
        }
        if (newLine.getProductName() == null || newLine.getProductName().isBlank()) {
            throw new IllegalArgumentException("productName must not be blank");
        }

        Cart cart = cartCommandsGateway.findCartById(new CartId(cart_id))
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Product product = catalogQuery.getProduct(newLine.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product " + newLine.getProductName()+ " not found"));


        CartLine lineToAdd = cart.getLines().get(product.getId());

        if (lineToAdd == null) {
            throw new IllegalArgumentException("Cart line not found for product " + product.getId().value());
        }

        domainService.addWithPolicy(
                cart,
                new ProductId(newLine.getProductId()),
                new ProductName(newLine.getProductName()),
                newLine.getNumberOfProducts(),
                product.getPrice()
        );

        cartCommandsGateway.save(cart);

        cart.getDomainEvents().forEach(publisher::publish);

        int qtySaved = lineToAdd.quantity();
        double subtotal = lineToAdd.subtotal();

        return new LineDTO(product.getId().value(),product.getName().name(),qtySaved,subtotal);
    }



}
