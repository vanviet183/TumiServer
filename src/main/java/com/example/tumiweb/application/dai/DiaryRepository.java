package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

  Set<Diary> findAllByUser_Id(Long userId);

  Set<Diary> findAllByStart(String start);

  List<Diary> findAllByDay(String day);

  List<Diary> findAllByUser_IdAndDay(Long userId, String day);

  Diary findByDay(String day);

}
