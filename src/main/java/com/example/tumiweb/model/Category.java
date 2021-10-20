package com.example.tumiweb.model;


import com.example.tumiweb.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category extends BaseEntity {

    @NotBlank
    private String name;
    private String description;
    private String seo;

    //link to table Course
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "category")
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

}
