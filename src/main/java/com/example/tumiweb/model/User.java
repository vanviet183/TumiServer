package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private String phone;
    


}
