package com.example.tumiweb.model;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;

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

    private String title;
    @NotBlank
    private String path;

    //link to table Course
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Course course;

}
