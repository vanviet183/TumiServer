package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.ChapterDTO;
import com.example.tumiweb.domain.entity.Chapter;
import com.example.tumiweb.domain.entity.Course;

import java.util.List;

public interface IChapterService {

  List<Chapter> findAllChapter(Long page, int size);

  List<Chapter> findAllChapterByCourseId(Long courseId, Long page, int size, boolean status, boolean both);

  Chapter findChapterById(Long id);

  Chapter createNewChapter(ChapterDTO chapterDTO, Long courseId);

  Chapter editChapterById(Long id, ChapterDTO chapterDTO);

  Chapter deleteChapterById(Long id);

  Chapter save(Chapter chapter);

  Course findCourseByChapterId(Long chapterId);

}
