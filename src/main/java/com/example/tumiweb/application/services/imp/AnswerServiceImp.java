package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
import com.example.tumiweb.application.dai.AnswerRepository;
import com.example.tumiweb.application.dai.QuestionRepository;
import com.example.tumiweb.application.mapper.AnswerMapper;
import com.example.tumiweb.application.services.IAnswerService;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.AnswerDTO;
import com.example.tumiweb.domain.entity.Answer;
import com.example.tumiweb.domain.entity.Question;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImp implements IAnswerService {
  private final AnswerMapper answerMapper = Mappers.getMapper(AnswerMapper.class);
  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;
  private final UploadFile uploadFile;

  public AnswerServiceImp(AnswerRepository answerRepository, QuestionRepository questionRepository,
                          UploadFile uploadFile) {
    this.answerRepository = answerRepository;
    this.questionRepository = questionRepository;
    this.uploadFile = uploadFile;
  }

  //  @Cacheable(value = "answer", key = "'questionId'+#questionId")
  @Override
  public List<Answer> findAllAnswerByQuestionId(Long questionId) {
    List<Answer> answers = answerRepository.findAllByQuestion_IdAndActiveFlagAndDeleteFlag(questionId, true, false);
    if (CollectionUtils.isEmpty(answers)) {
      throw new VsException(UserMessageConstant.ERR_NO_DATA_SELECT_RESULT, DevMessageConstant.Common.NO_DATA_SELECTED);
    }
    return answers;
  }

  //  @Cacheable(value = "answer", key = "#id")
  @Override
  public Answer findAnswerById(Long id) {
    Optional<Answer> answer = answerRepository.findById(id);
    if (answer.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "answer", id));
    }
    if (answer.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DELETE, id));
    }
    return answer.get();
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public Answer createNewAnswer(AnswerDTO answerDTO, Long questionId, MultipartFile multipartFile) {
    Question question = findQuestionById(questionId);

    Answer answer = answerMapper.toAnswer(answerDTO, null);
    if (multipartFile != null) {
      answer.setImage(uploadFile.getUrlFromFile(multipartFile));
    }
    answer.setQuestion(question);
    return answerRepository.save(answer);
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public List<Answer> createNewAnswers(Long questionId, List<AnswerDTO> answerDTOS, MultipartFile[] multipartFiles) {
    Question question = findQuestionById(questionId);
    List<Answer> answers = new ArrayList<>();

    for (int i = 0; i < answerDTOS.size(); i++) {
      if (multipartFiles[i] != null) {
        answerDTOS.get(i).setImage(uploadFile.getUrlFromFile(multipartFiles[i]));
      }
      answers.add(answerRepository.save(answerMapper.toAnswer(answerDTOS.get(i), null)));
    }

    return answers;
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public Answer editAnswerById(Long id, AnswerDTO answerDTO, MultipartFile multipartFile) {
    Answer answer = findAnswerById(id);

    if (multipartFile != null) {
      if (answer.getImage() != null) {
        uploadFile.removeImageFromUrl(answer.getImage());
      }
      answerDTO.setImage(uploadFile.getUrlFromFile(multipartFile));
    }

    return answerRepository.save(answerMapper.toAnswer(answerDTO, answer.getId()));
  }

  //  @CacheEvict(value = "answer", allEntries = true)
  @Override
  public Answer deleteAnswerById(Long id) {
    Answer answer = findAnswerById(id);

    Question question = answer.getQuestion();
    question.getAnswers().remove(answer);
    questionRepository.save(question);

    answer.setDeleteFlag(true);
    return answerRepository.save(answer);
  }

  //  @Cacheable(value = "answer", key = "'all'")
  @Override
  public List<Answer> findAllAnswer() {
    return answerRepository.findAll();
  }

  private Question findQuestionById(Long id) {
    Optional<Question> question = questionRepository.findById(id);
    if (question.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "question", id));
    }
    if (question.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DELETE, id));
    }
    if (question.get().getAnswers().size() >= 4) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          DevMessageConstant.Answer.FULL_ANSWER_FOR_THIS_QUESTION);
    }
    return question.get();
  }

}
