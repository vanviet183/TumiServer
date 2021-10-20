package com.example.tumiweb.model;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "answer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Answer extends BaseEntity {

    @NotBlank
    private String title;
    @NotBlank
    private Boolean isTrue = Boolean.FALSE;

    private String image;

    //link to table Question
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Question question;

}
