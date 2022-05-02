package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.application.dai.*;
import com.example.tumiweb.application.mapper.CourseMapper;
import com.example.tumiweb.application.services.ICourseService;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.CourseDTO;
import com.example.tumiweb.domain.entity.Category;
import com.example.tumiweb.domain.entity.Course;
import com.github.slugify.Slugify;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImp implements ICourseService {
  private final CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private ChapterRepository chapterRepository;
  @Autowired
  private UploadFile uploadFile;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private AnswerRepository answerRepository;
  @Autowired
  private Slugify slugify;

  //  @Cacheable(value = "course", key = "'all'")
  @Override
  public List<Course> findAllCourse(Long page, int size, boolean status, boolean both) {
    return courseRepository.findAll();
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course createNewCourse(CourseDTO courseDTO, MultipartFile multipartFile, Long categoryId) {
    if (courseRepository.findByName(courseDTO.getName()) != null) {
      throw new VsException("This name off course is contain");
    }

    Optional<Category> category = categoryRepository.findById(categoryId);
    if (category.isEmpty()) {
      throw new VsException("Can not find category by id: " + categoryId);
    }

    Course course = courseMapper.toCourse(courseDTO, null);
    if (multipartFile != null) {
      course.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    }

    course.setCategory(category.get());
    course.setSeo(slugify.slugify(course.getName()));

    return courseRepository.save(course);
  }

  //  @Cacheable(value = "course", key = "#id")
  @Override
  public Course findCourseById(Long id) {
    Optional<Course> course = courseRepository.findById(id);
    if (course.isEmpty()) {
      throw new VsException("Can not find course by id: " + id);
    }
    return course.get();
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course changeAvatarCourseById(Long id, MultipartFile multipartFile) {
    Course course = findCourseById(id);
    if (course.getAvatar() != null) {
      uploadFile.removeImageFromUrl(course.getAvatar());
    }

    if (!multipartFile.isEmpty()) {
      course.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    }

    return courseRepository.save(course);
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course editCourseById(Long id, CourseDTO courseDTO, MultipartFile multipartFile) {
    Course course = findCourseById(id);
    courseDTO.setSeo(slugify.slugify(courseDTO.getName()));

    if (multipartFile != null) {
      if (course.getAvatar() != null) {
        uploadFile.removeImageFromUrl(course.getAvatar());
      }
      course.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    }

    return courseRepository.save(courseMapper.toCourse(courseDTO, course.getId()));
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public TrueFalseResponse deleteCourseById(Long id) {
    Course course = findCourseById(id);
    course.getChapters().forEach(chapter -> {
      chapter.getQuestions().forEach(question -> {
        question.getAnswers().forEach(answer -> {
          answer.setDeleteFlag(true);
          answerRepository.save(answer);
        });
        question.setDeleteFlag(true);
        questionRepository.save(question);
      });
      chapter.setDeleteFlag(true);
      chapterRepository.save(chapter);
    });
    course.setDeleteFlag(true);
    courseRepository.save(course);
    return new TrueFalseResponse(true);
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course changeDeleteFlagById(Long id) {
    Course course = findCourseById(id);
    course.setDeleteFlag(!course.getDeleteFlag());

    return courseRepository.save(course);
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course editCategoryById(Long courseId, Long categoryId) {
    Course course = findCourseById(courseId);
    Optional<Category> category = categoryRepository.findById(categoryId);

    if (category.isEmpty()) {
      throw new VsException("Can not find category by id: " + categoryId);
    }

    if (course.getCategory() != null) {
      Optional<Category> oldCategory = categoryRepository.findById(course.getCategory().getId());
      oldCategory.ifPresent(value -> {
        value.getCourses().remove(course);
        categoryRepository.save(value);
      });
    }

    category.get().getCourses().add(course);

    course.setCategory(category.get());
    categoryRepository.save(category.get());

    return courseRepository.save(course);
  }

  //  @Cacheable(value = "course", key = "#key")
  @Override
  public List<Course> getCoursesByKey(String key) {
    return courseRepository.findAllByNameContainingOrDescriptionContaining(key, key);
  }

}