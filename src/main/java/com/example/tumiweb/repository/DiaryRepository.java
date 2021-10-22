package com.example.tumiweb.repository;

import com.example.tumiweb.dao.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Set<Diary> findAllByUser_Id(Long userId);
    Set<Diary> findAllByStart(String start);
    Set<Diary> findAllByDay(String day);
    Diary findByDay(String day);
//    Set<Diary> findByStart(String start);
}
