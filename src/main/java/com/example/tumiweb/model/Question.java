package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "question")
@Data
public class Question extends BaseEntity {

    private String title;
    private String image;

}
