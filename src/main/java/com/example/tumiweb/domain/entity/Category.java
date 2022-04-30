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
@Table(name = TableNameConstant.TBL_CATEGORY)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String name;

  @Nationalized
  private String description;

  private String seo;

  //link to table Course
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "category")
  @JsonIgnore
  private Set<Course> courses = new HashSet<>();

  public Category(Long id, String name, String description) {
    this.setId(id);
    this.name = name;
    this.description = description;
  }

}
