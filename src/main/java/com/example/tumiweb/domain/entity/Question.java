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
@Table(name = TableNameConstant.TBL_QUESTION)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String title;

  @NotBlank
  private String seo;

  private String avatar;

  //link to table Chapter
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Chapter chapter;

  //link to table Answer
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
  @JsonIgnore
  private Set<Answer> answers = new HashSet<>();

  public Question(Long id, String title, String seo, String avatar, Boolean deleteFlag) {
    this.setId(id);
    this.title = title;
    this.seo = seo;
    this.avatar = avatar;
    this.setDeleteFlag(deleteFlag);
  }

}
