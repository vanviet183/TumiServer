package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.TableNameConstant;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = TableNameConstant.TBL_GIFT)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Gift extends AbstractAuditingEntity {

  @NotBlank
  @Nationalized
  private String name;

  @Min(0)
  @Max(100)
  private Long mark;

  private String avatar;
  //link to table gift_order
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "gift_order_gift", joinColumns = @JoinColumn(name = "gift_id", referencedColumnName = CommonConstant.COLUMN_ID),
      inverseJoinColumns = @JoinColumn(name = "gift_order_id", referencedColumnName = CommonConstant.COLUMN_ID))
  @JsonIgnore
  private Set<GiftOrder> giftOrders = new HashSet<>();

  public Gift(Long id, String name, Long mark, String avatar) {
    this.setId(id);
    this.name = name;
    this.mark = mark;
    this.avatar = avatar;
  }

}
