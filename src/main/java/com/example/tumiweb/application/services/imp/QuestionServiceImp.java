package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.AnswerRepository;
import com.example.tumiweb.application.dai.ChapterRepository;
import com.example.tumiweb.application.dai.QuestionRepository;
import com.example.tumiweb.application.mapper.QuestionMapper;
import com.example.tumiweb.application.services.IQuestionService;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.QuestionDTO;
import com.example.tumiweb.domain.entity.Chapter;
import com.example.tumiweb.domain.entity.Question;
import com.github.slugify.Slugify;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImp implements IQuestionService {
  private final QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);
  @Autowired
  private QuestionRepository questionRepository;
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
  public List<Question> findAllQuestionByChapterId(Long chapterId, Long page, int size) {
    Optional<Chapter> chapterOptional = chapterRepository.findById(chapterId);
    if (chapterOptional.isEmpty()) {
      throw new VsException("Can not find chapter by id: " + chapterId);
    }
    if (chapterOptional.get().getDeleteFlag()) {
      throw new VsException("This chapter was delete");
    }
    List<Question> questions = questionRepository.findAllByChapter_IdAndDeleteFlagAndActiveFlag(chapterId, false, true);
    if (page != null) {
      Page<Question> questionPage = new PageImpl<>(questions, Pageable.ofSize(page.intValue()), questions.size());
      questions = questionPage.getContent();
    }

    return questions;
  }

  //  @Cacheable(value = "question", key = "#id")
  @Override
  public Question findQuestionById(Long id) {
    Optional<Question> question = questionRepository.findById(id);
    if (question.isEmpty()) {
      throw new VsException("Can not find question by id: " + id);
    }
    if (question.get().getDeleteFlag()) {
      throw new VsException("This question was delete");
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
    if (optional.get().getDeleteFlag()) {
      throw new VsException("Chapter was delete. Chapter id: " + chapterId);
    }
    Chapter chapter = optional.get();

    questionDTO.setSeo(slugify.slugify(questionDTO.getTitle()));
    Question question = questionMapper.toQuestion(questionDTO, null);
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
    Optional<Chapter> optional = chapterRepository.findById(question.getChapter().getId());
    if (optional.isEmpty()) {
      throw new VsException("Can not find chapter by id: " + question.getChapter().getId());
    }
    if (optional.get().getDeleteFlag()) {
      throw new VsException("Chapter was delete. Chapter id: " + questionId);
    }
    Chapter chapter = optional.get();

    if (multipartFile != null) {
      if (question.getAvatar() != null) {
        uploadFile.removeImageFromUrl(question.getAvatar());
      }
      questionDTO.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    }
    questionDTO.setSeo(slugify.slugify(questionDTO.getTitle()));

    question = questionRepository.save(questionMapper.toQuestion(questionDTO, question.getId()));
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
    question.getAnswers().forEach((item) -> {
      item.setDeleteFlag(true);
      answerRepository.save(item);
    });
    question.setDeleteFlag(true);
    return questionRepository.save(question);
  }

}
