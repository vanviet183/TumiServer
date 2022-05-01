package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.AnswerRepository;
import com.example.tumiweb.application.dai.ChapterRepository;
import com.example.tumiweb.application.dai.CourseRepository;
import com.example.tumiweb.application.dai.QuestionRepository;
import com.example.tumiweb.application.mapper.ChapterMapper;
import com.example.tumiweb.application.services.IChapterService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.ChapterDTO;
import com.example.tumiweb.domain.entity.Chapter;
import com.example.tumiweb.domain.entity.Course;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import com.github.slugify.Slugify;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChapterService implements IChapterService {
  private final ChapterMapper chapterMapper = Mappers.getMapper(ChapterMapper.class);
  @Autowired
  private ChapterRepository chapterRepository;
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private AnswerRepository answerRepository;
  @Autowired
  private Slugify slugify;

  //  @Cacheable(value = "chapter", key = "'all'")
  @Override
  public List<Chapter> findAllChapter(Long page, int size) {
    List<Chapter> chapters;
    if (page != null) {
      chapters = chapterRepository.findAll(PageRequest.of(page.intValue(), size)).getContent();
    } else {
      chapters = chapterRepository.findAll();
    }
    return chapters.parallelStream().filter(AbstractAuditingEntity::getActiveFlag).collect(Collectors.toList());
  }

  //  @Cacheable(value = "chapter", key = "'courseid'+#courseId")
  @Override
  public List<Chapter> findAllChapterByCourseId(Long courseId, Long page, int size, boolean deleteFlag, boolean both) {
    Optional<Course> courseOptional = courseRepository.findById(courseId);
    if (courseOptional.isEmpty()) {
      throw new VsException("Can not find course by id: " + courseId);
    }

    List<Chapter> chapters = chapterRepository.findAllByCourse_IdAndActiveFlag(courseId, true);

    if (!both && deleteFlag) {
      chapters = chapters.parallelStream().filter(item -> !item.getDeleteFlag()).collect(Collectors.toList());
    }

    if (page != null) {
      Page<Chapter> pageChapter = new PageImpl<>(chapters, Pageable.ofSize(page.intValue()), chapters.size());
      chapters = pageChapter.getContent();
    }

    return chapters;
  }

  //  @Cacheable(value = "chapter", key = "#id")
  @Override
  public Chapter findChapterById(Long id) {
    Optional<Chapter> chapter = chapterRepository.findById(id);
    if (chapter.isEmpty()) {
      throw new VsException("Can not find chapter by id: " + id);
    }
    if (!chapter.get().getActiveFlag()) {
      throw new VsException("This chapter is disable active");
    }
    return chapter.get();
  }

  //  @CacheEvict(value = "chapter", allEntries = true)
  @Override
  public Chapter createNewChapter(ChapterDTO chapterDTO, Long courseId) {
    Optional<Course> optional = courseRepository.findById(courseId);
    if (optional.isEmpty()) {
      throw new VsException("Can not find course by id: " + courseId);
    }
    Chapter chapter = chapterMapper.toChapter(chapterDTO);

    chapter.setSeo(slugify.slugify(chapter.getName()));
    chapter.setCourse(optional.get());

    return chapterRepository.save(chapter);
  }

  //  @CacheEvict(value = "chapter", allEntries = true)
  @Override
  public Chapter editChapterById(Long id, ChapterDTO chapterDTO) {
    Chapter chapter = findChapterById(id);

    chapter.setSeo(slugify.slugify(chapterDTO.getName()));
    chapter.setName(chapterDTO.getName());

    return chapterRepository.save(chapter);
  }

  //  @CacheEvict(value = "chapter", allEntries = true)
  @Override
  public Chapter deleteChapterById(Long id) {
    Chapter chapter = findChapterById(id);
    chapter.getQuestions().forEach(question -> {
      question.getAnswers().forEach(answer -> {
        answer.setDeleteFlag(true);
        answerRepository.save(answer);
      });
      question.setDeleteFlag(true);
      questionRepository.save(question);
    });
    chapter.setDeleteFlag(true);

    return chapterRepository.save(chapter);
  }

  @Override
  public Chapter save(Chapter chapter) {
    return chapterRepository.save(chapter);
  }

  //  @Cacheable(value = "chapter", key = "'chapterid'+#chapterId")
  @Override
  public Course findCourseByChapterId(Long chapterId) {
    return findChapterById(chapterId).getCourse();
  }

}
