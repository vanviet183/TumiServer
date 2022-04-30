package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.CourseRepository;
import com.example.tumiweb.application.services.ICategoryService;
import com.example.tumiweb.application.services.ICourseService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.utils.ConvertObject;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.CourseDTO;
import com.example.tumiweb.domain.dto.ImageDTO;
import com.example.tumiweb.domain.entity.Category;
import com.example.tumiweb.domain.entity.Course;
import com.example.tumiweb.domain.entity.Image;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseServiceImp implements ICourseService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private ChapterService chapterService;

  @Autowired
  private ImageServiceImp imageService;

  @Autowired
  private UploadFile uploadFile;

  @Autowired
  private ICategoryService categoryService;

  @Autowired
  private IUserService userService;

  @Autowired
  private Slugify slugify;

  //  @Cacheable(value = "course", key = "'all'")
  @Override
  public Set<Course> findAllCourse(Long page, int size, boolean status, boolean both) {
    return new HashSet<>(courseRepository.findAll());
  }

  //  @Cacheable(value = "course", key = "'userid'+#userId")
  @Override
  public Set<Course> findAllCourseByUserId(Long userId, boolean status, boolean both) {
    return userService.getUserById(userId).getCourses();
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course createNewCourse(CourseDTO courseDTO, MultipartFile multipartFile, Long categoryId) {
    if (courseRepository.findByName(courseDTO.getName()) != null) {
      throw new VsException("This name off course is contain");
    }
    Course course = modelMapper.map(courseDTO, Course.class);
    if (multipartFile != null) {
      Image image = imageService.createNewImage(new ImageDTO(multipartFile.getName()), multipartFile);
    }

    Category category = categoryService.findCategoryById(categoryId);
    if (category == null) {
      throw new VsException("Can not find category by id: " + categoryId);
    }
    course.setCategory(category);
    Course newCourse = courseRepository.save(course);

    categoryService.save(category);

    newCourse.setSeo(slugify.slugify(newCourse.getName()));

    return courseRepository.save(newCourse);
  }

  //  @Cacheable(value = "course", key = "#id")
  @Override
  public Course findCourseById(Long id) {
    Optional<Course> course = courseRepository.findById(id);
    if (course.isEmpty()) {
      return null;
    }
    return course.get();
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course changeAvatarCourseById(Long id, MultipartFile multipartFile) {
    Course course = findCourseById(id);
    if (course == null) {
      throw new VsException("Can not find course by id: " + id);
    }
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
    if (course == null) {
      throw new VsException("Can not find course by id: " + id);
    }
    courseDTO.setSeo(slugify.slugify(courseDTO.getName()));

    if (multipartFile != null) {
      if (course.getAvatar() != null) {
        uploadFile.removeImageFromUrl(course.getAvatar());
      }
      course.setAvatar(uploadFile.getUrlFromFile(multipartFile));
    }

    return courseRepository.save(ConvertObject.convertCourseDTOToCourse(course, courseDTO));
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course deleteCourseById(Long id) {
    Course course = findCourseById(id);
    if (course == null) {
      throw new VsException("Can not find course by id: " + id);
    }
    course.getChapters().forEach((item) -> {
      chapterService.deleteChapterById(Long.parseLong(item.getId().toString()));
    });
    return null;
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course changeStatusById(Long id) {
    Course course = findCourseById(id);

    if (course == null) {
      throw new VsException("Can not find course by id: " + id);
    }
    course.setDeleteFlag(!course.getDeleteFlag());

    return courseRepository.save(course);
  }

  //  @CacheEvict(value = "course", allEntries = true)
  @Override
  public Course editCategoryById(Long courseId, Long categoryId) {
    Course course = findCourseById(courseId);
    if (course == null) {
      throw new VsException("Can not find course by id: " + courseId);
    }

    Category category = categoryService.findCategoryById(categoryId);
    if (category == null) {
      throw new VsException("Can not find category by id: " + categoryId);
    }

    //xóa category cũ
    if (course.getCategory() != null) {
      Category oldCategory = categoryService.findCategoryById(course.getCategory().getId());
      if (oldCategory != null) {
        categoryService.save(oldCategory);
      }
    }

    category = categoryService.save(category);

    course.setCategory(category);
    return courseRepository.save(course);
  }

  //  @Cacheable(value = "course", key = "#key")
  @Override
  public List<Course> getCoursesByKey(String key) {
    return courseRepository.findAllByNameContainingOrDescriptionContaining(key, key);
  }

}
