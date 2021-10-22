package com.example.tumiweb.dao;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "help")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Help extends BaseEntity {
    @NotBlank
    @Nationalized
    private String title;

    //link to table Users
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}
