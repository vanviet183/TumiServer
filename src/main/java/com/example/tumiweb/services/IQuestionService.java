package com.example.tumiweb.services;

import com.example.tumiweb.dto.QuestionDTO;
import com.example.tumiweb.model.Question;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IQuestionService {
    Set<Question> findAllQuestionByChapterId(Long chapterId, Long page, int size);
    Question findQuestionById(Long id);
    Question createNewQuestion(QuestionDTO questionDTO, Long chapterId, MultipartFile multipartFile);
    Question editQuestionById(Long questionId, QuestionDTO questionDTO, MultipartFile multipartFile);
    Question save(Question question);

    //không cho delete nữa :v, lười r
    Question deleteQuestionById(Long id);

}
