package com.example.tumiweb.services;

import com.example.tumiweb.dto.AnswerDTO;
import com.example.tumiweb.model.Answer;

import java.util.Set;

public interface IAnswerService {
    Set<Answer> findAllAnswer(Long page, int size, boolean active);
    Set<Answer> findAllAnswerByQuestionId(Long questionId);
    Answer findAnswerById(Long id);
    Answer createNewAnswer(AnswerDTO answerDTO, Long questionId);
    Answer editAnswerById(Long id, AnswerDTO answerDTO);
    Answer deleteAnswerById(Long id);
}
