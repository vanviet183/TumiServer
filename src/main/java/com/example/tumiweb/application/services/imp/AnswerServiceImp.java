package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.AnswerRepository;
import com.example.tumiweb.application.services.IAnswerService;
import com.example.tumiweb.application.services.IQuestionService;
import com.example.tumiweb.application.utils.ConvertObject;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.AnswerDTO;
import com.example.tumiweb.domain.entity.Answer;
import com.example.tumiweb.domain.entity.Question;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AnswerServiceImp implements IAnswerService {
  private final AnswerRepository answerRepository;
  private final IQuestionService questionService;
  private final ModelMapper modelMapper;
  private final UploadFile uploadFile;

  public AnswerServiceImp(AnswerRepository answerRepository, IQuestionService questionService,
                          ModelMapper modelMapper, UploadFile uploadFile) {
    this.answerRepository = answerRepository;
    this.questionService = questionService;
    this.modelMapper = modelMapper;
    this.uploadFile = uploadFile;
  }

  //  @Cacheable(value = "answer", key = "'questionId'+#questionId")
  @Override
  public Set<Answer> findAllAnswerByQuestionId(Long questionId) {
    Set<Answer> answers = answerRepository.findAllByQuestion_Id(questionId);
    if (answers.isEmpty()) {
      throw new VsException("Answer list is empty");
    }
    return answers;
  }

  //  @Cacheable(value = "answer", key = "#id")
  @Override
  public Answer findAnswerById(Long id) {
    Optional<Answer> answer = answerRepository.findById(id);
    if (answer.isEmpty()) {
      return null;
    }
    return answer.get();
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public Answer createNewAnswer(AnswerDTO answerDTO, Long questionId, MultipartFile multipartFile) {
    Question question = questionService.findQuestionById(questionId);
    if (question == null) {
      throw new VsException("Can not find question by id: " + questionId);
    }

    if (question.getAnswers().size() >= 4) {
      throw new VsException("This question is full answer");
    }
    Answer answer = modelMapper.map(answerDTO, Answer.class);
    if (multipartFile != null) {
      answer.setImage(uploadFile.getUrlFromFile(multipartFile));
    }
    answer.setQuestion(question);
    return answerRepository.save(answer);
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public Set<Answer> createNewAnswers(Long questionId, List<AnswerDTO> answerDTOS, MultipartFile[] multipartFiles) {
    Question question = questionService.findQuestionById(questionId);

    if (question == null) {
      throw new VsException("Can not find question by id: " + questionId);
    }

    System.out.println("Hihi: " + question.getAnswers().size());
    if (question.getAnswers().size() >= 4) {
      throw new VsException("This question is full answer");
    }

    Set<Answer> answers = new HashSet<>();

    for (int i = 0; i < answerDTOS.size(); i++) {
      if (multipartFiles[i] != null) {
        answerDTOS.get(i).setImage(uploadFile.getUrlFromFile(multipartFiles[i]));
      }
      answers.add(answerRepository.save(modelMapper.map(answerDTOS.get(i), Answer.class)));
    }

    return answers;
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public Answer editAnswerById(Long id, AnswerDTO answerDTO, MultipartFile multipartFile) {
    Answer answer = findAnswerById(id);
    if (answer == null) {
      throw new VsException("Can not find Answer by id: " + id);
    }

    if (multipartFile != null) {
      if (answer.getImage() != null) {
        uploadFile.removeImageFromUrl(answer.getImage());
      }
      answerDTO.setImage(uploadFile.getUrlFromFile(multipartFile));
    }

    return answerRepository.save(ConvertObject.convertAnswerDTOToAnswer(answer, answerDTO));
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public Answer deleteAnswerById(Long id) {
    Answer answer = findAnswerById(id);
    if (answer == null) {
      throw new VsException("Can not find Answer by id: " + id);
    }

    Question question = answer.getQuestion();
    questionService.save(question);

    answerRepository.delete(answer);
    return answer;
  }

  //  @Cacheable(value = "answer", key = "'all'")
  @Override
  public List<Answer> findAllAnswer() {
    return answerRepository.findAll();
  }

}
