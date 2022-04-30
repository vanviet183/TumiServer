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

@Entity
@Table(name = TableNameConstant.TBL_IMAGE)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Image extends AbstractAuditingEntity {

  @Nationalized
  private String title;

  @NotBlank
  private String path;

  //link to table Course
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Course course;

}
