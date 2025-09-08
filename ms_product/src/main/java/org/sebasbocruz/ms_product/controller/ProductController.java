package org.sebasbocruz.ms_product.controller;

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
@RequestMapping("arka/v1/product")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService,ProductMapper productMapper){
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {

            List<Product> products = productService.getAllProducts();

            if (products == null || products.isEmpty()) {
                log.info("There weren't found products");
                return ResponseEntity
                        .noContent()
                        .header("X-Total-Count", "0")
                        .header("Cache-Control", "no-cache")
                        .build();
            }

            List<ProductDTO> productDTOs = productMapper.mapFromEntityToDTO(products);

            return ResponseEntity
                    .ok()
                    .header("X-Total-Count", String.valueOf(products.size()))
                    .header("Cache-Control", "public, max-age=300")
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(productDTOs);

        } catch (DataAccessException e) {
            log.error("Database error trying to get products: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Type", "DATABASE_ERROR")
                    .build();

        } catch (Exception e) {
            log.error("Unexpected Error trying to get the products: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Type", "INTERNAL_ERROR")
                    .build();
        }
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@RequestParam String category) {

        try {
            log.info("Request Got it for trying to get products by Category: {}", category);

            if(category.isBlank()){
                throw new IllegalArgumentException("Category can not be empty");
            }

            String normalizedCategory = category.trim().toLowerCase();

            List<Product> products = productService.getProductsByCategory(normalizedCategory);

            if (products == null || products.isEmpty()) {
                log.info("There weren't found products for the category: {}", normalizedCategory);
                return ResponseEntity
                        .notFound()
                        .header("X-Total-Count", "0")
                        .header("X-Category", normalizedCategory)
                        .header("Cache-Control", "public, max-age=600")
                        .build();
            }

            List<ProductDTO> productDTOs = productMapper.mapFromEntityToDTO(products);

            log.info("There were found {} products for the category: {}", products.size(), normalizedCategory);

            return ResponseEntity
                    .ok()
                    .header("X-Total-Count", String.valueOf(products.size()))
                    .header("X-Category", normalizedCategory)
                    .header("Cache-Control", "public, max-age=300")
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(productDTOs);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid Category: {}", category, e);
            return ResponseEntity
                    .badRequest()
                    .header("X-Error-Type", "INVALID_CATEGORY")
                    .header("X-Error-Message", "Invalid Category: " + e.getMessage())
                    .build();

        } catch (DataAccessException e) {
            log.error("Database error trying to get products by category {}: {}", category, e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Type", "DATABASE_ERROR")
                    .header("X-Category", category)
                    .build();

        } catch (Exception e) {
            log.error("Unexpected error for the category {}: {}", category, e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Type", "INTERNAL_ERROR")
                    .header("X-Category", category)
                    .build();
        }
    }

}
