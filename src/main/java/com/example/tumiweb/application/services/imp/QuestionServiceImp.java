package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.AnswerRepository;
import com.example.tumiweb.application.dai.ChapterRepository;
import com.example.tumiweb.application.dai.QuestionRepository;
import com.example.tumiweb.application.services.IQuestionService;
import com.example.tumiweb.application.utils.ConvertObject;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.QuestionDTO;
import com.example.tumiweb.domain.entity.Chapter;
import com.example.tumiweb.domain.entity.Question;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
  private UploadFile uploadFile;
  @Autowired
  private Slugify slugify;

  //  @Cacheable(value = "question", key = "'chapter'+#chapterId")
  @Override
  public Set<Question> findAllQuestionByChapterId(Long chapterId, Long page, int size) {
    Optional<Chapter> optional = chapterRepository.findById(chapterId);
    if (optional.isEmpty()) {
      throw new VsException("Can not find chapter by id: " + chapterId);
    }
    Chapter chapter = optional.get();
    List<Question> questions;
    if (page != null) {
      questions = questionRepository.findAll(PageRequest.of(page.intValue(), size)).getContent();
    } else {
      questions = questionRepository.findAll();
    }

    return questions.stream().filter(item -> item.getChapter().equals(chapter))
        .collect(Collectors.toSet());
  }

  //  @Cacheable(value = "question", key = "#id")
  @Override
  public Question findQuestionById(Long id) {
    Optional<Question> question = questionRepository.findById(id);
    if (question.isEmpty()) {
      return null;
    }
    return question.get();
  }

  //  @CacheEvict(value = "question", allEntries = true)
  @Override
  public Question createNewQuestion(QuestionDTO questionDTO, Long chapterId, MultipartFile multipartFile) {
    Optional<Chapter> optional = chapterRepository.findById(chapterId);
    if (optional.isEmpty()) {
      throw new VsException("Can not find chapter by id: " + chapterId);
    }
    Chapter chapter = optional.get();

    questionDTO.setSeo(slugify.slugify(questionDTO.getTitle()));
    Question question = modelMapper.map(questionDTO, Question.class);
    question.setChapter(chapter);

    if (multipartFile != null) {
      question.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    }

    question = questionRepository.save(question);

    chapterRepository.save(chapter);

    return questionRepository.save(question);
  }

  //  @CacheEvict(value = "question", allEntries = true)
  @Override
  public Question editQuestionById(Long questionId, QuestionDTO questionDTO, MultipartFile multipartFile) {
    Question question = findQuestionById(questionId);
    if (question == null) {
      throw new VsException("Can not find question by id: " + questionId);
    }

    Optional<Chapter> optional = chapterRepository.findById(question.getChapter().getId());
    if (optional.isEmpty()) {
      throw new VsException("Can not find chapter by id: " + question.getChapter().getId());
    }
    Chapter chapter = optional.get();

    if (multipartFile != null) {
      if (question.getAvatar() != null) {
        uploadFile.removeImageFromUrl(question.getAvatar());
      }
      questionDTO.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    }

    questionDTO.setSeo(slugify.slugify(questionDTO.getTitle()));

    question = questionRepository.save(ConvertObject.convertQuestionDTOToQuestion(question, questionDTO));

    chapterRepository.save(chapter);
    return questionRepository.save(question);
  }

  //  @CacheEvict(value = "question", allEntries = true)
  @Override
  public Question save(Question question) {
    return questionRepository.save(question);
  }

  //  @CacheEvict(value = "question", allEntries = true)
  @Override
  public Question deleteQuestionById(Long id) {
    Question question = findQuestionById(id);
    if (question == null) {
      throw new VsException("Can not find question by id: " + id);
    }
    question.getAnswers().forEach((item) -> {
      answerRepository.delete(item);
    });
    questionRepository.delete(question);
    return question;
  }

}
