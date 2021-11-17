package com.example.tumiweb.services;

import com.example.tumiweb.dao.Course;
import com.example.tumiweb.dto.ChapterDTO;
import com.example.tumiweb.dao.Chapter;

import java.util.Set;

public interface IChapterService {
    Set<Chapter> findAllChapter(Long page, int size);
    Set<Chapter> findAllChapterByCourseId(Long courseId, Long page, int size, boolean status, boolean both);
    Chapter findChapterById(Long id);
    Chapter createNewChapter(ChapterDTO chapterDTO, Long courseId);
    Chapter editChapterById(Long id, ChapterDTO chapterDTO);
    Chapter deleteChapterById(Long id);

    Chapter save(Chapter chapter);

    Course findCourseByChapterId(Long chapterId);
}
