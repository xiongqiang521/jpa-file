package com.xq.domain;

import javax.persistence.*;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/25 17:47
 */
@Entity
public class AuthorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}
