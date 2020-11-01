package com.xq.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/24 23:19
 */
@Table(name = "t_manufacturer")
@Data
@Entity
public class ManufacturerDTO {
    @Id
    private String id;
    private String name;
}
