package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "course")
@Data
public class Course extends BaseEntity{

    private String name;
    private Long price;
    private String description;
    private String avatar;
    private Long process;
    private String seo;

}
