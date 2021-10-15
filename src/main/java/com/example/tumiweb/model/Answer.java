package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "answer")
@Data
public class Answer extends BaseEntity {

    private String title;
    private Boolean isTrue;

    //link to table Question
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Question question;
}
