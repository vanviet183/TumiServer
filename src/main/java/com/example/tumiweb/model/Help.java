package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "help")
@Data
public class Help extends BaseEntity{
    private String title;
}
