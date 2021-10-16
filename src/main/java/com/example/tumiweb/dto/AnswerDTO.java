package com.example.tumiweb.dto;

import com.example.tumiweb.model.Question;
import lombok.Data;

@Data
public class AnswerDTO {
    private String title;
    private Boolean isTrue = Boolean.FALSE;
}
