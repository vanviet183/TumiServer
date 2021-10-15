package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "image")
@Data
public class Image extends BaseEntity {

    private String title;
    private String path;

}
