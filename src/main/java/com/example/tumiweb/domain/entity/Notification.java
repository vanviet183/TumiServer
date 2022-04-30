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
@Table(name = TableNameConstant.TBL_NOTIFICATION)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String title;

  private String path;

  //link to table User
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = CommonConstant.COLUMN_USER_ID)
  private User user;

  public Notification(Long id, String title, String path) {
    this.setId(id);
    this.title = title;
    this.path = path;
  }

}
