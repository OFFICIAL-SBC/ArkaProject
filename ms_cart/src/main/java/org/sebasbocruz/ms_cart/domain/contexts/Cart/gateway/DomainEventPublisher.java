package org.sebasbocruz.ms_cart.domain.contexts.Cart.gateway;

public interface DomainEventPublisher {
    void publish(Object event);
}
