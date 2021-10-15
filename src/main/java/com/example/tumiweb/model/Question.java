package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "question")
@Data
public class Question extends BaseEntity {

    private String title;
    private String image;

    //link to table Chapter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Chapter chapter;

    //link to table Image
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
    private Set<Image> images = new HashSet<>();

    //link to table Answer
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();

}
