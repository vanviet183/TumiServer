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
@Table(name = TableNameConstant.TBL_CHAPTER)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Chapter extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String name;

  private String seo;

  //link to table course
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Course course;

  //link to table Question
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chapter")
  @JsonIgnore
  private Set<Question> questions = new HashSet<>();

  public Chapter(Long id, String name) {
    this.setId(id);
    this.name = name;
  }

}
