package com.example.tumiweb.model;

import com.example.tumiweb.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chapter")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Chapter extends BaseEntity {

    @NotBlank
    private String name;
    private String seo;

    //link to table course
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Course course;

    //link to table Question
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chapter")
    @JsonIgnore
    private Set<Question> questions = new HashSet<>();

    public void addRelationQuestion(Question question) {
        questions.add(question);
        question.setChapter(this);
    }
    public void deleteRelationQuestion(Question question) {
        questions.remove(question);
        question.setChapter(null);
    }

}
