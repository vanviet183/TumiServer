package com.example.tumiweb.dto;

import com.example.tumiweb.model.Answer;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class QuestionDTO {
    private String title;
    private Set<AnswerDTO> answers = new HashSet<>();
}
