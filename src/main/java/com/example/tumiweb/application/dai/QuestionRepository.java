package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findAllByChapter_IdAndDeleteFlagAndActiveFlag(Long chapterId, Boolean deleteFlag, Boolean activeFlag);

}
