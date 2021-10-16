package com.example.tumiweb.repository;

import com.example.tumiweb.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Set<Answer> findAllByQuestion_Id(Long questionId);
    Set<Answer> findAllByStatus(boolean status);
}
