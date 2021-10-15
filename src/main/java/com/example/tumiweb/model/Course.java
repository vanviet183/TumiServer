package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    //link to table user
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "courses")
    private Set<User> users = new HashSet<>();

    //link to table category
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Category category;

    //link to table image
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    private Set<Image> images = new HashSet<>();

    //link to table Chapter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    private Set<Chapter> chapters = new HashSet<>();

}
