package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.AnswerDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Answer;
import com.example.tumiweb.model.GiftOrder;
import com.example.tumiweb.repository.AnswerRepository;
import com.example.tumiweb.repository.QuestionRepository;
import com.example.tumiweb.services.IAnswerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnswerServiceImp implements IAnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Set<Answer> findAllAnswer(Long page, int size, boolean active) {
        List<Answer> answers;
        if(page != null) {
            Page<Answer> answerPage = answerRepository.findAll(PageRequest.of(page.intValue(), size));
            answers = answerPage.getContent();
        }else {
            answers = answerRepository.findAll();
        }

        if(active) {
            answers = new ArrayList<>(answerRepository.findAllByStatus(true));
            if(page != null) {
                int length = answers.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                answers = answers.subList(page.intValue()*size, page.intValue()*size + size);
            }else {
                answers = new ArrayList<>(answerRepository.findAllByStatus(true));
            }
        }
        return new HashSet<>(answers);
    }

    @Override
    public Set<Answer> findAllAnswerByQuestionId(Long questionId) {
        Set<Answer> answers = answerRepository.findAllByQuestion_Id(questionId);
        if(answers.isEmpty()) {
            throw new NotFoundException("Answer list is empty");
        }
        return answers;
    }

    @Override
    public Answer findAnswerById(Long id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if(answer.isEmpty()) {
            return null;
        }
        return answer.get();
    }

    @Override
    public Answer createNewAnswer(AnswerDTO answerDTO, Long questionId) {
        Answer answer = modelMapper.map(answerDTO, Answer.class);
        answer.setQuestion(questionRepository.getById(questionId));
        return answerRepository.save(answer);
    }

    @Override
    public Answer editAnswerById(Long id, AnswerDTO answerDTO) {
        Answer answer = findAnswerById(id);
        if(answer == null) {
            throw new NotFoundException("Can not find Answer by id: " + id);
        }
        return answerRepository.save(modelMapper.map(answerDTO, Answer.class));
    }

    @Override
    public Answer deleteAnswerById(Long id) {
        Answer answer = findAnswerById(id);
        if(answer == null) {
            throw new NotFoundException("Can not find Answer by id: " + id);
        }
        answerRepository.delete(answer);
        return answer;
    }
}
