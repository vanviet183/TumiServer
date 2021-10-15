package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Data
public class Image extends BaseEntity {

    private String title;
    private String path;

    //link to table Course
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Course course;

    //link to table Question
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Question question;
}
