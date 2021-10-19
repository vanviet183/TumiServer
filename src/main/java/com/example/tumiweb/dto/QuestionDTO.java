package com.example.tumiweb.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuestionDTO {
    private String title;
    private Set<AnswerDTO> answers = new HashSet<>();
}
