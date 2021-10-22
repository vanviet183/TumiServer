package com.example.tumiweb.dao;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Nationalized;

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
    @Nationalized
    private String title;

    private Boolean isTrue;

    private String image;

    //link to table Question
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Question question;

}
