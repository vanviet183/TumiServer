package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.AnswerDTO;
import com.example.tumiweb.domain.entity.Answer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IAnswerService {

  Set<Answer> findAllAnswerByQuestionId(Long questionId);

  Answer findAnswerById(Long id);

  Answer createNewAnswer(AnswerDTO answerDTO, Long questionId, MultipartFile multipartFile);

  Set<Answer> createNewAnswers(Long questionId, List<AnswerDTO> answerDTOS, MultipartFile[] multipartFiles);

  Answer editAnswerById(Long id, AnswerDTO answerDTO, MultipartFile multipartFile);

  Answer deleteAnswerById(Long id);

  List<Answer> findAllAnswer();

}
