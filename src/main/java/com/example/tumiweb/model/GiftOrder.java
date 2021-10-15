package com.example.tumiweb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gift_order")
@Data
public class GiftOrder extends BaseEntity {

    private Long quality;
    private String email;

}
