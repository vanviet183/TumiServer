package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.CourseDTO;
import com.example.tumiweb.dto.ImageDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Category;
import com.example.tumiweb.dao.Course;
import com.example.tumiweb.dao.Image;
import com.example.tumiweb.repository.CourseRepository;
import com.example.tumiweb.services.ICategoryService;
import com.example.tumiweb.services.ICourseService;
import com.example.tumiweb.utils.ConvertObject;
import com.example.tumiweb.utils.UploadFile;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
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
    private Slugify slugify;

    @Override
    public Set<Course> findAllCourse(Long page, int size, boolean status, boolean both) {
        return new HashSet<>(courseRepository.findAll());
    }

    @Override
    public Set<Course> findAllCourseByUserId(Long userId, boolean status, boolean both) {
        return null;
    }

    @Override
    public Course createNewCourse(CourseDTO courseDTO, MultipartFile multipartFile, Long categoryId) {
        if(courseRepository.findByName(courseDTO.getName()) != null) {
            throw new DuplicateException("This name off course is contain");
        }
        Course course = modelMapper.map(courseDTO, Course.class);
        if(multipartFile != null) {
            Image image = imageService.createNewImage(new ImageDTO(multipartFile.getName()), multipartFile);
            course.addRelationImage(image);
        }

        Category category = categoryService.findCategoryById(categoryId);
        if(category == null) {
            throw new NotFoundException("Can not find category by id: " + categoryId);
        }
        course.setCategory(category);

        course.setSeo(slugify.slugify(course.getName()));

        return courseRepository.save(course);
    }

    @Override
    public Course findCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if(course.isEmpty()) {
            return null;
        }
        return course.get();
    }

    @Override
    public Course changeAvatarCourseById(Long id, MultipartFile multipartFile) {
        Course course = findCourseById(id);
        if(course == null) {
            throw new NotFoundException("Can not find course by id: " + id);
        }
        if(course.getAvatar() != null) {
            uploadFile.removeImageFromUrl(course.getAvatar());
        }

        if(!multipartFile.isEmpty()) {
            course.setAvatar(uploadFile.getUrlFromFile(multipartFile));
        }

        return courseRepository.save(course);
    }

    @Override
    public Course editCourseById(Long id, CourseDTO courseDTO, MultipartFile multipartFile) {
        Course course = findCourseById(id);
        if(course == null) {
            throw new NotFoundException("Can not find course by id: " + id);
        }
        courseDTO.setSeo(slugify.slugify(courseDTO.getName()));

        if(multipartFile != null) {
            if(course.getAvatar() != null) {
                uploadFile.removeImageFromUrl(course.getAvatar());
            }
            course.setAvatar(uploadFile.getUrlFromFile(multipartFile));
        }

        return courseRepository.save(ConvertObject.convertCourseDTOToCourse(course, courseDTO));
    }

    @Override
    public Course deleteCourseById(Long id) {
        Course course = findCourseById(id);
        if(course == null) {
            throw new NotFoundException("Can not find course by id: " + id);
        }
        course.getChapters().forEach((item) -> {
            course.deleteRelationChapter(item);
            chapterService.deleteChapterById(Long.parseLong(item.getId().toString()));
        });
        return null;
    }

    @Override
    public Course changeStatusById(Long id) {
        Course course = findCourseById(id);

        if(course == null) {
            throw new NotFoundException("Can not find course by id: " + id);
        }
        course.setStatus(!course.getStatus());

        return courseRepository.save(course);
    }

    @Override
    public Course editCategoryById(Long courseId, Long categoryId) {
        Course course = findCourseById(courseId);
        if(course == null) {
            throw new NotFoundException("Can not find course by id: " + courseId);
        }

        Category category = categoryService.findCategoryById(categoryId);
        if(category == null) {
            throw new NotFoundException("Can not find category by id: " + categoryId);
        }

        //xóa category cũ
        if(course.getCategory() != null) {
            Category oldCategory = categoryService.findCategoryById(course.getCategory().getId());
            if(oldCategory != null) {
                oldCategory.deleteRelationCourse(course);
                categoryService.save(oldCategory);
            }
        }

        category.addRelationCourse(course);
        category = categoryService.save(category);

        course.setCategory(category);
        return courseRepository.save(course);
    }
}