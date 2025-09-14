package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway.out;

public interface DomainEventPublisher {
    void publish(Object event);
}
