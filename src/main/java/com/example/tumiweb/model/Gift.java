package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gift")
@Data
public class Gift extends BaseEntity {

    private String title;

    //link to table User
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gifts")
    private Set<User> users = new HashSet<>();

    //link to table gift_order
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "gift_order_gift",
            joinColumns = @JoinColumn(name = "gift_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "gift_order_id", referencedColumnName = "id")
    )
    private Set<GiftOrder> giftOrders = new HashSet<>();

}
