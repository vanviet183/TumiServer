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
@Table(name = "chapter")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Chapter extends BaseEntity {

    @NotBlank
    @Nationalized
    private String name;
    private String seo;

    public Chapter(Long id, String name, Boolean status) {
        this.setId(id);
        this.name = name;
        this.setStatus(status);
    }

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
