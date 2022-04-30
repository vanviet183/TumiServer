package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.TableNameConstant;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = TableNameConstant.TBL_COURSE)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Course extends AbstractAuditingEntity {

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
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Category category;

  //link to table image
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
  @JsonIgnore
  private Set<Image> images = new HashSet<>();

  //link to table Chapter
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
  @JsonIgnore
  private Set<Chapter> chapters = new HashSet<>();

  public Course(Long id, String name, Long price, String description, String avatar, Long process) {
    this.setId(id);
    this.name = name;
    this.price = price;
    this.description = description;
    this.avatar = avatar;
    this.process = process;
  }

}
