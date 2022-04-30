package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.TableNameConstant;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = TableNameConstant.TBL_ROLE)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String name;

  @NotBlank
  private String description;

  //link to table Users
  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "roles")
  @JsonIgnore
  private Set<User> users = new HashSet<>();

}
