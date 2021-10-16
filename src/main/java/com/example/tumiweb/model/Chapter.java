package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chapter")
@Data
public class Chapter extends BaseEntity {

    @NotBlank
    private String name;
    private String seo;

    //link to table course
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Course course;

    //link to table Question
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chapter")
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
