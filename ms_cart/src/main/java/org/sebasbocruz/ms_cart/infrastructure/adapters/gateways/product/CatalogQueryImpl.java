package org.sebasbocruz.ms_cart.infrastructure.adapters.gateways.product;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_cart.domain.contexts.Product.entities.Product;
import org.sebasbocruz.ms_cart.domain.contexts.Product.gateway.CatalogQuery;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.mapper.ProductMapper;
import org.sebasbocruz.ms_cart.infrastructure.adapters.persistence.repositories.ProductEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatalogQueryImpl implements CatalogQuery {

    // TODO: Is this the right way to do it? Should we use a repository or a client to another microservice?
    // TODO: is this the domain of the ms_cart? or should we have a separate ms_product that handles all the product related queries and commands?

    private final ProductEntityRepository productEntityRepository;

    @Override
    public Optional<Product> getProduct(Long product_id) {
        return productEntityRepository.findById(product_id).map( ProductMapper::fromInfrastructureToDomain);
    }
}
