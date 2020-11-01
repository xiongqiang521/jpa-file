package com.xq.domain;

import javax.persistence.*;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private AuthorInfo info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorInfo getInfo() {
        return info;
    }

    public void setInfo(AuthorInfo info) {
        this.info = info;
    }
}
