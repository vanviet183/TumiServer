package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.TableNameConstant;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = TableNameConstant.TBL_GIFT_ORDER)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GiftOrder extends AbstractAuditingEntity {

  private Long quality;

  @Email
  private String email;

  //link to table User
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private User user;

  //link to table gift
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "giftOrders")
  @JsonIgnore
  private Set<Gift> gifts = new HashSet<>();

}
