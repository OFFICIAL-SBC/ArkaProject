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


    private final ProductEntityRepository productEntityRepository;

    @Override
    public Optional<Product> getProduct(String productName) {
        return productEntityRepository.findByName(productName).map( ProductMapper::fromInfrastructureToDomain);
    }
}
