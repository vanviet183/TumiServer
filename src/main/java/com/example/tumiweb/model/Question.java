package com.example.tumiweb.model;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "question")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question extends BaseEntity {

    @NotBlank
    private String title;

    //link to table Chapter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Chapter chapter;


    //link to table Image
    @OneToOne()
    @JoinColumn(name = "image_id")
    private Image image;


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
