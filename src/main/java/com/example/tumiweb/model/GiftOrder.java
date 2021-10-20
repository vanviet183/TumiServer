package com.example.tumiweb.model;

import com.example.tumiweb.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gift_order")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GiftOrder extends BaseEntity {

    private Long quality;
    @Email
    private String email;


    //link to table User
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;


    //link to table gift
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "giftOrders")
    @JsonIgnore
    private Set<Gift> gifts = new HashSet<>();

}
