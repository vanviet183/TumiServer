package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.entity.Diary;
import com.example.tumiweb.domain.entity.User;

import java.util.List;
import java.util.Set;

public interface IDiaryService {

  Set<Diary> findAllByUserId(Long userId);

  List<Diary> findAllByUserIdAndOnDay(Long userId, String day);

  Diary findDiaryById(Long id);

  Diary createNewDiary(Long idUser);

  Diary editDiaryById(Long id);

  Diary deleteDiaryById(Long id);

  List<Diary> findAll();

  Set<User> findAllUserByDay(String day);

}
