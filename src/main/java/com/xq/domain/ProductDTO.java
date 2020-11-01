package com.xq.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.List;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/24 16:04
 */
@Data
@Entity
@Table(name="t_product")
public class ProductDTO {

    @Id
    @GeneratedValue(generator  = "myIdStrategy")
    @GenericGenerator(name = "myIdStrategy", strategy = "uuid")
    private String productId;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ServiceDTO> services;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_manufacturer")
    private ManufacturerDTO manufacturer;


}

