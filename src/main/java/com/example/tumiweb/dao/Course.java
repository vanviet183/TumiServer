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
@Table(name = "course")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course extends BaseEntity {

    @NotBlank
    @Nationalized
    private String name;
    private Long price;
    @Nationalized
    private String description;
    private String avatar;
    private Long process;
    private String seo;

    //link to table user
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "courses")
    @JsonIgnore
    private Set<User> users = new HashSet<>();


    //link to table category
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Category category;


    //link to table image
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    @JsonIgnore
    private Set<Image> images = new HashSet<>();

    public void addRelationImage(Image image) {
        images.add(image);
        image.setCourse(this);
    }
    public void deleteRelationImage(Image image) {
        images.remove(image);
        image.setCourse(null);
    }


    //link to table Chapter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    @JsonIgnore
    private Set<Chapter> chapters = new HashSet<>();

    public void addRelationChapter(Chapter chapter) {
        chapters.add(chapter);
        chapter.setCourse(this);
    }
    public void deleteRelationChapter(Chapter chapter) {
        chapters.remove(chapter);
        chapter.setCourse(null);
    }

}
