package org.sebasbocruz.ms_product.repository;

import org.sebasbocruz.ms_product.model.Category;
import org.sebasbocruz.ms_product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product,Long> {

    List<Product> findProductByCategory_NameIgnoreCase(String categoryName);

}
