package com.example.tumiweb.model;

import com.example.tumiweb.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gift")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Gift extends BaseEntity {

    @NotBlank
    private String name;
    @Min(0)
    @Max(100)
    private Long mark;
    private String avatar;


    //link to table gift_order
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "gift_order_gift",
            joinColumns = @JoinColumn(name = "gift_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "gift_order_id", referencedColumnName = "id")
    )
    private Set<GiftOrder> giftOrders = new HashSet<>();

}
