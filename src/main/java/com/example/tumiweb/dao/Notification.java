package com.example.tumiweb.dao;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "notification")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Transactional
public class Notification extends BaseEntity {

    @NotBlank
    @Nationalized
    private String title;
    private String path;


    //link to table User
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private User user;

}
