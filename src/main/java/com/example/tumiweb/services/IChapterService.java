package com.example.tumiweb.services;

import com.example.tumiweb.dto.ChapterDTO;
import com.example.tumiweb.model.Chapter;

import java.util.Set;

public interface IChapterService {
    Set<Chapter> findAllChapter(Long page, int size);
    Chapter findChapterById(Long id);
    Chapter createNewChapter(ChapterDTO chapterDTO, Long courseId);
    Chapter editChapterById(Long id, ChapterDTO chapterDTO);
    Chapter deleteChapterById(Long id);
}
