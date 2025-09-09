package org.sebasbocruz.ms_product.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.sebasbocruz.ms_product.dto.ProductDTO;
import org.sebasbocruz.ms_product.mappers.ProductMapper;
import org.sebasbocruz.ms_product.model.Product;
import org.sebasbocruz.ms_product.service.ProductService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        if (products == null || products.isEmpty()) {
            log.info("There weren't found products");
            throw new EntityNotFoundException("There weren't products found");
        }

        List<ProductDTO> productDTOs = productMapper.mapFromEntityToDTO(products);

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(products.size()))
                .header("Cache-Control", "public, max-age=300")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(productDTOs);
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@RequestParam String category) {
        log.info("Request received for getting products by category: {}", category);

        if (category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }

        String normalizedCategory = category.trim().toLowerCase();
        List<Product> products = productService.getProductsByCategory(normalizedCategory);

        if (products == null || products.isEmpty()) {
            log.info("No products found for category: {}", normalizedCategory);
            throw new EntityNotFoundException("No products found for category");
        }

        List<ProductDTO> productDTOs = productMapper.mapFromEntityToDTO(products);
        log.info("Found {} products for category: {}", products.size(), normalizedCategory);

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(products.size()))
                .header("X-Category", normalizedCategory)
                .header("Cache-Control", "public, max-age=300")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(productDTOs);
    }

    @GetMapping("/brand")
    public ResponseEntity<List<ProductDTO>> getProductsByBrand(@RequestParam String brand) {
        log.info("Request received for getting products by brand: {}", brand);

        if (brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }

        String normalizedBrand = brand.trim().toLowerCase();
        List<Product> products = productService.getProductsByBrand(normalizedBrand);

        if (products == null || products.isEmpty()) {
            log.info("No products found for brand: {}", normalizedBrand);
            throw new EntityNotFoundException("No products found for brand");
        }

        List<ProductDTO> productDTOs = productMapper.mapFromEntityToDTO(products);
        log.info("Found {} products for brand: {}", products.size(), normalizedBrand);

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(products.size()))
                .header("X-Brand", normalizedBrand)
                .header("Cache-Control", "public, max-age=300")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(productDTOs);
    }
}