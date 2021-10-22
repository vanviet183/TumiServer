package com.example.tumiweb.dao;

import com.example.tumiweb.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role extends BaseEntity {

    @NotBlank
    @Nationalized
    private String name;
    private String description;

    //link to table Users
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

}
