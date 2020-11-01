package com.xq.service;

import com.xq.domain.ProductDTO;
import com.xq.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/25 0:03
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public List<ProductDTO> getProduct() {
        return productRepository.findAll();

    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        ProductDTO save = productRepository.save(productDTO);
        return save;
    }
}
