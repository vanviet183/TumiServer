package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gift")
@Data
public class Gift extends BaseEntity {

    private String title;

}
