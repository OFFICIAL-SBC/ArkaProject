package org.sebasbocruz.ms_product.service;

import org.sebasbocruz.ms_product.model.Product;
import org.sebasbocruz.ms_product.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category){
        return productRepository.findProductByCategory_NameIgnoreCase(category);
    }

    public List<Product> getProductsByBrand(String brand){
        return productRepository.findProductByBrand_NameIgnoreCase(brand);
    }


}
