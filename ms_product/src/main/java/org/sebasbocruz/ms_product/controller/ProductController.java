package org.sebasbocruz.ms_product.controller;

import org.sebasbocruz.ms_product.dto.ProductDTO;
import org.sebasbocruz.ms_product.mappers.ProductMapper;
import org.sebasbocruz.ms_product.model.Product;
import org.sebasbocruz.ms_product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("arka/v1/product")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService,ProductMapper productMapper){
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        List<Product> result = productService.getAllProducts();
        return ResponseEntity.ok(productMapper.mapFromEntityToDTO(result));
    }


}
