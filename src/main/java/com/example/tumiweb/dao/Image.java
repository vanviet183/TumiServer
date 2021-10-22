package com.example.tumiweb.dao;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "image")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Image extends BaseEntity {
    @Nationalized
    private String title;
    @NotBlank
    private String path;

    //link to table Course
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Course course;

}
