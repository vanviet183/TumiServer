package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.AnswerDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Answer;
import com.example.tumiweb.dao.Question;
import com.example.tumiweb.repository.AnswerRepository;
import com.example.tumiweb.services.IAnswerService;
import com.example.tumiweb.services.IQuestionService;
import com.example.tumiweb.utils.ConvertObject;
import com.example.tumiweb.utils.UploadFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class AnswerServiceImp implements IAnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UploadFile uploadFile;

    @Cacheable(value = "answer", key = "'questionId'+#questionId")
    @Override
    public Set<Answer> findAllAnswerByQuestionId(Long questionId) {
        Set<Answer> answers = answerRepository.findAllByQuestion_Id(questionId);
        if(answers.isEmpty()) {
            throw new NotFoundException("Answer list is empty");
        }
        return answers;
    }

    @Cacheable(value = "answer", key = "#id")
    @Override
    public Answer findAnswerById(Long id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if(answer.isEmpty()) {
            return null;
        }
        return answer.get();
    }

    @CacheEvict(value = "answer", allEntries = true)
    @Override
    public Answer createNewAnswer(AnswerDTO answerDTO, Long questionId, MultipartFile multipartFile) {
        Question question = questionService.findQuestionById(questionId);
        if(question == null) {
            throw new NotFoundException("Can not find question by id: " + questionId);
        }

        if(question.getAnswers().size() >= 4) {
            throw new NotFoundException("This question is full answer");
        }
        Answer answer = modelMapper.map(answerDTO, Answer.class);
        if(multipartFile != null) {
            answer.setImage(uploadFile.getUrlFromFile(multipartFile));
        }
        answer.setQuestion(question);
        return answerRepository.save(answer);
    }

    @CacheEvict(value = "answer", allEntries = true)
    @Override
    public Set<Answer> createNewAnswers(Long questionId, List<AnswerDTO> answerDTOS, MultipartFile[] multipartFiles) {
        Question question = questionService.findQuestionById(questionId);

        if(question == null) {
            throw new NotFoundException("Can not find question by id: " + questionId);
        }

        System.out.println("Hihi: " + question.getAnswers().size());
        if(question.getAnswers().size() >= 4) {
            throw new NotFoundException("This question is full answer");
        }

        Set<Answer> answers = new HashSet<>();

        for(int i=0; i<answerDTOS.size(); i++) {
            if(multipartFiles[i] != null) {
                answerDTOS.get(i).setImage(uploadFile.getUrlFromFile(multipartFiles[i]));
            }
            answers.add(answerRepository.save(modelMapper.map(answerDTOS.get(i), Answer.class)));
        }

        return answers;
    }

    @CacheEvict(value = "answer", allEntries = true)
    @Override
    public Answer editAnswerById(Long id, AnswerDTO answerDTO, MultipartFile multipartFile) {
        Answer answer = findAnswerById(id);
        if(answer == null) {
            throw new NotFoundException("Can not find Answer by id: " + id);
        }

        if(multipartFile != null) {
            if(answer.getImage() != null) {
                uploadFile.removeImageFromUrl(answer.getImage());
            }
            answerDTO.setImage(uploadFile.getUrlFromFile(multipartFile));
        }

        return answerRepository.save(ConvertObject.convertAnswerDTOToAnswer(answer, answerDTO));
    }

    @CacheEvict(value = "answer", allEntries = true)
    @Override
    public Answer deleteAnswerById(Long id) {
        Answer answer = findAnswerById(id);
        if(answer == null) {
            throw new NotFoundException("Can not find Answer by id: " + id);
        }

        Question question = answer.getQuestion();
        question.deleteRelation(answer);
        questionService.save(question);

        answerRepository.delete(answer);
        return answer;
    }

    @Cacheable(value = "answer", key = "'all'")
    @Override
    public List<Answer> findAllAnswer() {
        return answerRepository.findAll();
    }
}
