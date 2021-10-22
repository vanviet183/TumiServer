package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.QuestionDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Chapter;
import com.example.tumiweb.dao.Question;
import com.example.tumiweb.repository.AnswerRepository;
import com.example.tumiweb.repository.QuestionRepository;
import com.example.tumiweb.services.IChapterService;
import com.example.tumiweb.services.IQuestionService;
import com.example.tumiweb.utils.ConvertObject;
import com.example.tumiweb.utils.UploadFile;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

//Phần này chưa sửa repository của các bảng liên quan thành service :v
@Service
public class QuestionServiceImp implements IQuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IChapterService chapterService;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UploadFile uploadFile;

    @Autowired
    private Slugify slugify;

    @Override
    public Set<Question> findAllQuestionByChapterId(Long chapterId, Long page, int size) {
        Chapter chapter = chapterService.findChapterById(chapterId);
        if(chapter == null) {
            throw new NotFoundException("Can not find chapter by id: " + chapterId);
        }
        List<Question> questions;
        if(page != null) {
            questions = questionRepository.findAll(PageRequest.of(page.intValue(), size)).getContent();
        }else {
            questions = questionRepository.findAll();
        }

        return questions.stream().filter(item -> {
            return item.getChapter().equals(chapter);
        }).collect(Collectors.toSet());
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
    public Question createNewQuestion(QuestionDTO questionDTO, Long chapterId, MultipartFile multipartFile) {
        Chapter chapter = chapterService.findChapterById(chapterId);
        if(chapter == null) {
            throw new NotFoundException("Can not find chapter by id: " + chapterId);
        }
        questionDTO.setSeo(slugify.slugify(questionDTO.getTitle()));
        Question question = modelMapper.map(questionDTO, Question.class);
        question.setChapter(chapter);

        if(multipartFile != null) {
            question.setAvatar(uploadFile.getUrlFromFile(multipartFile));
        }

        question = questionRepository.save(question);
        chapter.addRelationQuestion(question);

        chapterService.save(chapter);

        return questionRepository.save(question);
    }

    @Override
    public Question editQuestionById(Long questionId, QuestionDTO questionDTO, MultipartFile multipartFile) {
        Question question = findQuestionById(questionId);
        if(question == null) {
            throw new NotFoundException("Can not find question by id: " + questionId);
        }

        Chapter chapter = chapterService.findChapterById(question.getChapter().getId());
        if(chapter == null) {
            throw new NotFoundException("Can not find chapter by id: " + question.getChapter().getId());
        }

        if(multipartFile != null) {
            if(question.getAvatar() != null) {
                uploadFile.removeImageFromUrl(question.getAvatar());
            }
            questionDTO.setAvatar(uploadFile.getUrlFromFile(multipartFile));
        }

        questionDTO.setSeo(slugify.slugify(questionDTO.getTitle()));

        question = questionRepository.save(ConvertObject.convertQuestionDTOToQuestion(question, questionDTO));
        chapter.addRelationQuestion(question);

        chapterService.save(chapter);
        return questionRepository.save(question);
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
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
