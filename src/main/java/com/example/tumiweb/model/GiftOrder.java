package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gift_order")
@Data
public class GiftOrder extends BaseEntity {

    private Long quality;
    @Email
    private String email;

    //link to table gift
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "giftOrders")
    private Set<Gift> gifts = new HashSet<>();

}
