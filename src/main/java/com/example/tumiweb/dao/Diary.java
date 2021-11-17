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
public class Diary extends BaseEntity {
    @NotBlank
    private String start;
    private String end;
    private String day;

    public Diary(Long id, String start, String end, String day) {
        this.setId(id);
        this.start = start;
        this.end = end;
        this.day = day;
    }

    //link to table Users
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private User user;
}
