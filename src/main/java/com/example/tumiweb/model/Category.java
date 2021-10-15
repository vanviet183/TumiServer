package com.example.tumiweb.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "category")
@Data
public class Category extends BaseEntity {

    private String name;
    private String description;
    private String seo;

}
