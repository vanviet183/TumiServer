package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.QuestionDTO;
import com.example.tumiweb.domain.entity.Question;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IQuestionService {

  List<Question> findAllQuestionByChapterId(Long chapterId, Long page, int size);

  Question findQuestionById(Long id);

  Question createNewQuestion(QuestionDTO questionDTO, Long chapterId, MultipartFile multipartFile);

  Question editQuestionById(Long questionId, QuestionDTO questionDTO, MultipartFile multipartFile);

  Question save(Question question);

  Question deleteQuestionById(Long id);

}
