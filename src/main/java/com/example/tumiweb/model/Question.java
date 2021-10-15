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

    public void addRelationImage(Image image) {
        images.add(image);
        image.setQuestion(this);
    }
    public void deleteRelationImage(Image image) {
        images.remove(image);
        image.setQuestion(null);
    }


    //link to table Answer
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();

    public void addRelationAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }
    public void deleteRelation(Answer answer) {
        answers.remove(answer);
        answer.setQuestion(null);
    }

}
