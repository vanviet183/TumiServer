package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "answer")
@Data
public class Answer extends BaseEntity {

    @NotBlank
    private String title;
    @NotBlank
    private Boolean isTrue = Boolean.FALSE;

    //link to table Question
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Question question;


    //link to table image
    @OneToOne()
    @JoinColumn(name = "image_id")
    private Image image;
}
