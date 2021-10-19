package com.example.tumiweb.dto;

import com.example.tumiweb.model.Gift;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiftOrderDTO {
    private Long quality;
    private String email;
    private Set<Gift> gifts = null;
}
