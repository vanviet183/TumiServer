package com.example.tumiweb.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnswerDTO {
    private String title;
    private Boolean isTrue = Boolean.FALSE;
    private String image;
}
