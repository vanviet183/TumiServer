package com.example.tumiweb.dao;


import com.example.tumiweb.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Nationalized;

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
    @Nationalized
    private String name;
    @Nationalized
    private String description;
    private String seo;

    public Category(Long id, String name, String description, boolean status) {
        this.setId(id);
        this.name = name;
        this.description = description;
        this.setStatus(status);
    }


    //link to table Course
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "category")
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    public void addRelationCourse(Course course) {
        courses.add(course);
        course.setCategory(this);
    }

    public void deleteRelationCourse(Course course) {
        courses.remove(course);
        course.setCategory(null);
    }

}
