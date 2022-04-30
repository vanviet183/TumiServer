package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.TableNameConstant;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = TableNameConstant.TBL_HELP)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Help extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String title;

  //link to table Users
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinColumn(name = CommonConstant.COLUMN_USER_ID)
  private User user;

  public Help(Long id, String title) {
    this.setId(id);
    this.title = title;
  }

}
