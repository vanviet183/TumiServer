package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.ChapterRepository;
import com.example.tumiweb.application.dai.CourseRepository;
import com.example.tumiweb.application.mapper.ChapterMapper;
import com.example.tumiweb.application.services.IChapterService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.ChapterDTO;
import com.example.tumiweb.domain.entity.Chapter;
import com.example.tumiweb.domain.entity.Course;
import com.github.slugify.Slugify;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ChapterService implements IChapterService {
  private final ChapterMapper chapterMapper = Mappers.getMapper(ChapterMapper.class);
  @Autowired
  private ChapterRepository chapterRepository;
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private QuestionServiceImp questionService;
  @Autowired
  private Slugify slugify;

  //  @Cacheable(value = "chapter", key = "'all'")
  @Override
  public Set<Chapter> findAllChapter(Long page, int size) {
    if (page != null) {
      return new HashSet<>(chapterRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
    }
    return new HashSet<>(chapterRepository.findAll());
  }

  //  @Cacheable(value = "chapter", key = "'courseid'+#courseId")
  @Override
  public Set<Chapter> findAllChapterByCourseId(Long courseId, Long page, int size, boolean status, boolean both) {
    Optional<Course> optional = courseRepository.findById(courseId);
    if (optional.isEmpty()) {
      throw new VsException("Can not find course by id: " + courseId);
    }
    Course course = optional.get();

//        //Đang đau đầu quá, copy cho nhanh khi nào rảnh thì vào sửa
//        Set<Chapter> chapters = new HashSet<>();
//        //paging
//        if(page != null) {
//            //có paging
//            if(both) {
//                chapters = new HashSet<>(chapterRepository.findAll(PageRequest.of(page.intValue(), size))
//                .getContent());
//            }else if(status) {
//                chapters = chapterRepository.findAllByStatus(true);
//                int length = chapters.size();
//                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
//                if(totalPage > page.intValue()) {
//                    return new HashSet<>();
//                }
//                chapters = new HashSet<>(new ArrayList<>(chapters).subList(page.intValue()*size, page.intValue()
//                *size + size));
//            }else {
//                chapters = chapterRepository.findAllByStatus(false);
//                int length = chapters.size();
//                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
//                if(totalPage > page.intValue()) {
//                    return new HashSet<>();
//                }
//                chapters = new HashSet<>(new ArrayList<>(chapters).subList(page.intValue()*size, page.intValue()
//                *size + size));
//            }
//        }else {
//            chapters = new HashSet<>(chapterRepository.findAll());
//        }

    return chapterRepository.findAllByCourse_Id(courseId);
  }

  //  @Cacheable(value = "chapter", key = "#id")
  @Override
  public Chapter findChapterById(Long id) {
    Optional<Chapter> chapter = chapterRepository.findById(id);
    if (chapter.isEmpty()) {
      return null;
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
    Course course = optional.get();
    Chapter chapter = chapterMapper.toChapter(chapterDTO);

    chapter.setSeo(slugify.slugify(chapter.getName()));

    chapter.setCourse(course);
    return chapterRepository.save(chapter);
  }

  //  @CacheEvict(value = "chapter", allEntries = true)
  @Override
  public Chapter editChapterById(Long id, ChapterDTO chapterDTO) {
    Chapter chapter = findChapterById(id);
    if (chapter == null) {
      throw new VsException("Can not find Chapter by id: " + id);
    }

    chapter.setSeo(slugify.slugify(chapterDTO.getName()));
    chapter.setName(chapterDTO.getName());
    return chapterRepository.save(chapter);
  }

  //  @CacheEvict(value = "chapter", allEntries = true)
  @Override
  public Chapter deleteChapterById(Long id) {
    Chapter chapter = findChapterById(id);
    if (chapter == null) {
      throw new VsException("Can not find Chapter by id: " + id);
    }
    chapter.getQuestions().forEach((item) -> {
      questionService.deleteQuestionById(Long.parseLong(item.getId().toString()));
    });
    chapterRepository.delete(chapter);
    return chapter;
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
