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
@Table(name = "question")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question extends BaseEntity {

    @NotBlank
    @Nationalized
    private String title;
    private String seo;
    private String avatar;

    //link to table Chapter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Chapter chapter;



    //link to table Answer
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
    @JsonIgnore
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
