package com.xq.controller;

import com.xq.domain.ProductDTO;
import com.xq.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/24 16:03
 */
@RestController
public class IndexController {
    @Autowired
    private ProductService productService;

    @RequestMapping(path = "" , method = RequestMethod.GET)
    public List<ProductDTO> getProduct(){
        return productService.getProduct();
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO){
        System.out.println(productDTO);

        return productService.saveProduct(productDTO);
    }
}
