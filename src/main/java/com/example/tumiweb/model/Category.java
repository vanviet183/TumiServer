package com.example.tumiweb.model;


import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Data
public class Category extends BaseEntity {

    private String name;
    private String description;
    private String seo;

    //link to table Course
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "category")
    private Set<Course> courses = new HashSet<>();

}
