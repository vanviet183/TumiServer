package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Data
public class Role extends BaseEntity{

    private String name;
    private String description;

    //link to table Users
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<User> users = new HashSet<>();

}
