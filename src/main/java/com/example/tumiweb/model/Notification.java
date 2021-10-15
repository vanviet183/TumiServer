package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "notification")
@Data
public class Notification extends BaseEntity{

    private String title;
    private String path;

    //link to table User

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private User user;

}
