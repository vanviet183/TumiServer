package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.AnswerDTO;
import com.example.tumiweb.dto.QuestionDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Answer;
import com.example.tumiweb.model.Image;
import com.example.tumiweb.model.Question;
import com.example.tumiweb.repository.AnswerRepository;
import com.example.tumiweb.repository.ChapterRepository;
import com.example.tumiweb.repository.ImageRepository;
import com.example.tumiweb.repository.QuestionRepository;
import com.example.tumiweb.services.IQuestionService;
import com.example.tumiweb.utils.UploadFile;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

//Phần này chưa sửa repository của các bảng liên quan thành service :v
@Service
public class QuestionServiceImp implements IQuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UploadFile uploadFile;

    @Autowired
    private Slugify slugify;

    @Override
    public Set<Question> findAllQuestion(Long page, int size) {
        if(page != null) {
            return new HashSet<>(questionRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
        }
        return new HashSet<>(questionRepository.findAll());
    }

    @Override
    public Question findQuestionById(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if(question.isEmpty()) {
            return null;
        }
        return question.get();
    }

    @Override
    public Question createNewQuestion(QuestionDTO questionDTO, Long chapterId, Set<AnswerDTO> answerDTOS, MultipartFile multipartFile) {
        questionDTO.setSeo(slugify.slugify(questionDTO.getTitle()));
        Question question = modelMapper.map(questionDTO, Question.class);
        question.setChapter(chapterRepository.getById(chapterId));

        if(!multipartFile.isEmpty()) {
            Image image = new Image();
            image.setPath(uploadFile.getUrlFromFile(multipartFile));
            image.setTitle(multipartFile.getName());
            question.setImage(imageRepository.save(image));
        }
        Set<Answer> answers = new HashSet<>();
        answerDTOS.forEach(item -> {
            answers.add(answerRepository.save(modelMapper.map(item, Answer.class)));
        });
        question.setAnswers(answers);

        return questionRepository.save(question);
    }

    @Override
    public Question editQuestionById(QuestionDTO questionDTO, Long chapterId, Set<AnswerDTO> answerDTOS, MultipartFile multipartFile) {
        return null;
    }

    @Override
    public Question deleteQuestionById(Long id) {
        Question question = findQuestionById(id);
        if(question == null) {
            throw new NotFoundException("Can not find question by id: " + id);
        }
        question.getAnswers().forEach((item) -> {
            question.deleteRelation(item);
            answerRepository.delete(item);
        });
        questionRepository.delete(question);
        return question;
    }
}
