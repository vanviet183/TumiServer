package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.TableNameConstant;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = TableNameConstant.TBL_ANSWER)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Answer extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String title;

  private Boolean isTrue = Boolean.FALSE;

  private String image;

  //link to table Question
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Question question;

  public Answer(Long id, String title, Boolean isTrue, String image) {
    this.setId(id);
    this.title = title;
    this.isTrue = isTrue;
    this.image = image;
  }

}
