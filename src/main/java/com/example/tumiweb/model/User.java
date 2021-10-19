package com.example.tumiweb.model;

import com.example.tumiweb.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User extends BaseEntity {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;
    private String phone;
    private String avatar;

    private Long mark = 0L;

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
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();


    //link to table Gift
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_order_gift",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "gift_order_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<GiftOrder> giftOrders = new HashSet<>();


    //link to table Course
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_course",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();



}
