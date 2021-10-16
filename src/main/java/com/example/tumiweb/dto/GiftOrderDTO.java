package com.example.tumiweb.dto;

import com.example.tumiweb.model.Gift;
import lombok.Data;
import java.util.Set;

@Data
public class GiftOrderDTO {
    private Long quality;
    private String email;
    private Set<Gift> gifts = null;
}
