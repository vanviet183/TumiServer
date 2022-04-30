package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.AuthenticationProvider;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = TableNameConstant.TBL_USER)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractAuditingEntity {

  @NotBlank
  private String username;

  @JsonIgnore
  @NotBlank
  private String password;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Nationalized
  private String fullName;

  private String phone;

  private String avatar;

  private String birthday;

  private Long mark = 0L;

  @Enumerated(EnumType.STRING)
  private AuthenticationProvider authProvider;

  @JsonIgnore
  private String tokenResetPass;

  //link to table Notification
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private Set<Notification> notifications = new HashSet<>();

  //link to table Help
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private Set<Help> helps = new HashSet<>();

  //link to table Role
  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  //phải để EAGER k thì sẽ lỗi "could not initialize proxy – no Session"
  @JoinTable(name = TableNameConstant.TBL_USER_ROLE, joinColumns = @JoinColumn(name = CommonConstant.COLUMN_USER_ID,
      referencedColumnName = CommonConstant.COLUMN_ID),
      inverseJoinColumns = @JoinColumn(name = CommonConstant.COLUMN_ROLE_ID, referencedColumnName = CommonConstant.COLUMN_ID))
  @JsonIgnore
  private Set<Role> roles = new HashSet<>();

  //link to table Gift-Order
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private Set<GiftOrder> giftOrders = new HashSet<>();

  //link to table Course
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = TableNameConstant.TBL_USER_COURSE, joinColumns = @JoinColumn(name = CommonConstant.COLUMN_USER_ID,
      referencedColumnName = CommonConstant.COLUMN_ID),
      inverseJoinColumns = @JoinColumn(name = CommonConstant.COLUMN_COURSE_ID, referencedColumnName = CommonConstant.COLUMN_ID))
  @JsonIgnore
  private Set<Course> courses = new HashSet<>();

  //link to table diary
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
  @JsonIgnore
  private Set<Diary> diaries = new HashSet<>();

  public User(Long id, String username, String password, String fullName, String email, String phone, String avatar,
              Long mark, String birthday, Boolean deleteFlag) {
    this.setId(id);
    this.username = username;
    this.password = password;
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.avatar = avatar;
    this.mark = mark;
    this.birthday = birthday;
    this.setDeleteFlag(deleteFlag);
  }

}
