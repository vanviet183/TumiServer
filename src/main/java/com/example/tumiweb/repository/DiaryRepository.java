package com.example.tumiweb.repository;

import com.example.tumiweb.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Set<Diary> findAllByStart(String start);
    Set<Diary> findAllByUser_Id(Long userId);
    Set<Diary> findByStart(String start);
}
