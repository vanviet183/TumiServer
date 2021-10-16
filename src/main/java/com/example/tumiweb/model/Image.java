package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "image")
@Data
public class Image extends BaseEntity {

    private String title;
    @NotBlank
    private String path;

    //link to table Course
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Course course;

}
