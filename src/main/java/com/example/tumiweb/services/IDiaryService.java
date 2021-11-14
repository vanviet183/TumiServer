package com.example.tumiweb.services;

import com.example.tumiweb.dao.Diary;
import com.example.tumiweb.dao.User;

import java.util.List;
import java.util.Set;

public interface IDiaryService {
    Set<Diary> findAllByUserId(Long userId);
    List<Diary> findAllByUserIdAndOnDay(Long userId, String day);
    Diary findDiaryById(Long id);
    Diary createNewDiary(Long idUser);
    Diary editDiaryById(Long id);
    Diary deleteDiaryById(Long id);

    //send mail sau 1 ngày không học
    Set<User> findAllUserByDay(String day);
}
