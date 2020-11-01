package com.xq.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/24 23:19
 */
@Data
@Entity
@Table(name = "t_service")
public class ServiceDTO {
    @Id
    private String id;

    private String name;

}
