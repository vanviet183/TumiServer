package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
@Data
public class Notification extends BaseEntity{

    private String title;
    private String path;

}
