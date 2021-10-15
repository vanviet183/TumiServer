package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "answer")
@Data
public class Answer extends BaseEntity {

    private String title;
    private Boolean isTrue;

}
