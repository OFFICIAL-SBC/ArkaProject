package org.sebasbocruz.ms_product.repository;

import org.sebasbocruz.ms_product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product,Long> {
}
