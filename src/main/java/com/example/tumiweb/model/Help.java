package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "help")
@Data
public class Help extends BaseEntity{
    @NotBlank
    private String title;

    //link to table Users
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}
