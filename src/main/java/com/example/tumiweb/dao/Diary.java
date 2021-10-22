package com.example.tumiweb.dao;


import com.example.tumiweb.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "diary")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Diary extends BaseEntity {
    @NotBlank
    private String start;
    private String end;
    private String day;

    //link to table Users
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private User user;
}
