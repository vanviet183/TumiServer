package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

  List<Answer> findAllByQuestion_IdAndActiveFlagAndDeleteFlag(Long questionId, Boolean activeFlag, Boolean deleteFlag);

  List<Answer> findAllByDeleteFlag(Boolean flag);

}
