package com.example.tumiweb.domain.entity;


import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.TableNameConstant;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = TableNameConstant.TBL_DIARY)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Diary extends AbstractAuditingEntity {

  @NotBlank
  private String start;
  private String end;
  private String day;

  //link to table Users
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = CommonConstant.COLUMN_USER_ID)
  private User user;

  public Diary(Long id, String start, String end, String day) {
    this.setId(id);
    this.start = start;
    this.end = end;
    this.day = day;
  }

}
