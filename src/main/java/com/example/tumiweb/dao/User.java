package com.example.tumiweb.dao;

import com.example.tumiweb.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class User extends BaseEntity {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String fullName;
    private String phone;
    private String avatar;
    private String birthday;

    private Long mark = 0L;

    public User(Long id, String username, String password, String fullName, String email, String phone, String avatar, Long mark, String birthday, boolean status) {
        this.setId(id);
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.mark = mark;
        this.birthday = birthday;
        this.setStatus(status);
    }

    @Column(name = "token_reset_password")
    @JsonIgnore
    private String tokenResetPass;

    //link to table Notification
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    public void addRelationNotification(Notification notification) {
        notifications.add(notification);
        notification.setUser(this);
    }
    public void deleteRelationNotification(Notification notification) {
        notifications.remove(notification);
        notification.setUser(null);
    }



    //link to table Help
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Help> helps = new HashSet<>();

    public void addRelationHelp(Help help) {
        helps.add(help);
        help.setUser(this);
    }
    public void deleteRelationHelp(Help help) {
        helps.remove(help);
        help.setUser(null);
    }


    //link to table Role
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//phải để EAGER k thì sẽ lỗi "could not initialize proxy – no Session"
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();



    //link to table Gift-Order
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<GiftOrder> giftOrders = new HashSet<>();
    public void addRelationGiftOrder(GiftOrder giftOrder) {
        giftOrders.add(giftOrder);
        giftOrder.setUser(this);
    }
    public void deleteRelationGiftOrder(GiftOrder giftOrder) {
        giftOrders.remove(giftOrder);
        giftOrder.setUser(null);
    }



    //link to table Course
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_course",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();



    //link to table diary
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Diary> diaries = new HashSet<>();

    public void addRelationDiary(Diary diary) {
        diaries.add(diary);
        diary.setUser(this);
    }
    public void deleteRelationDiary(Diary diary) {
        diaries.remove(diary);
        diary.setUser(null);
    }

}
