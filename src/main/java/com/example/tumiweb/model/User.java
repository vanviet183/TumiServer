package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;

    //link to table Notification
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Notification> notifications;

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
    private Set<Help> helps;

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
    private Set<Role> roles = new HashSet<>();


    //link to table Gift
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_gift",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "gift_id", referencedColumnName = "id")
    )
    private Set<Gift> gifts = new HashSet<>();


    //link to table Course
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_course",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private Set<Course> courses = new HashSet<>();


}
