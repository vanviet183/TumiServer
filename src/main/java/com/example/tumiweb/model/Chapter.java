package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "chapter")
@Data
public class Chapter extends BaseEntity {

    private String name;
    private String seo;

}
