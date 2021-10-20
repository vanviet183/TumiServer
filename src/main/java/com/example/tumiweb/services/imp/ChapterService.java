package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.ChapterDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Chapter;
import com.example.tumiweb.model.Course;
import com.example.tumiweb.repository.ChapterRepository;
import com.example.tumiweb.repository.CourseRepository;
import com.example.tumiweb.services.IChapterService;
import com.example.tumiweb.services.ICourseService;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChapterService implements IChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseServiceImp courseService;

    @Autowired
    private QuestionServiceImp questionService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Slugify slugify;

    @Override
    public Set<Chapter> findAllChapter(Long page, int size) {
        if(page != null) {
            return new HashSet<>(chapterRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
        }
        return new HashSet<>(chapterRepository.findAll());
    }

    @Override
    public Set<Chapter> findAllChapterByCourseId(Long courseId, Long page, int size, boolean status, boolean both) {
        Course course = courseService.findCourseById(courseId);
        if(course == null) {
            throw new NotFoundException("Can not find course by id: " + courseId);
        }

        //Đang đau đầu quá, copy cho nhanh khi nào rảnh thì vào sửa
        Set<Chapter> chapters;
        //paging
        if(page != null) {
            //có paging
            if(both) {
                chapters = new HashSet<>(chapterRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
            }else if(status) {
                chapters = chapterRepository.findAllByStatus(true);
                int length = chapters.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                chapters = new HashSet<>(new ArrayList<>(chapters).subList(page.intValue()*size, page.intValue()*size + size));
            }else {
                chapters = chapterRepository.findAllByStatus(false);
                int length = chapters.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                chapters = new HashSet<>(new ArrayList<>(chapters).subList(page.intValue()*size, page.intValue()*size + size));
            }
        }else {
            chapters = new HashSet<>(chapterRepository.findAll());
        }

        return chapters.stream().filter(item -> {
            return item.getCourse().equals(course);
        }).collect(Collectors.toSet());
    }

    @Override
    public Chapter findChapterById(Long id) {
        Optional<Chapter> chapter = chapterRepository.findById(id);
        if(chapter.isEmpty()) {
            return null;
        }
        return chapter.get();
    }

    @Override
    public Chapter createNewChapter(ChapterDTO chapterDTO, Long courseId) {
        Course course = courseService.findCourseById(courseId);
        if(course == null) {
            throw new NotFoundException("Can not find course by id: " + courseId);
        }
        Chapter chapter = modelMapper.map(chapterDTO, Chapter.class);

        chapter.setSeo(slugify.slugify(chapter.getName()));

        chapter.setCourse(course);
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter editChapterById(Long id, ChapterDTO chapterDTO) {
        Chapter chapter = findChapterById(id);
        if(chapter == null) {
            throw new NotFoundException("Can not find Chapter by id: " + id);
        }

        chapter.setSeo(slugify.slugify(chapterDTO.getName()));
        chapter.setName(chapterDTO.getName());
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter deleteChapterById(Long id) {
        Chapter chapter = findChapterById(id);
        if(chapter == null) {
            throw new NotFoundException("Can not find Chapter by id: " + id);
        }
        chapter.getQuestions().forEach((item) -> {
            chapter.deleteRelationQuestion(item);
            questionService.deleteQuestionById(Long.parseLong(item.getId().toString()));
        });
        chapterRepository.delete(chapter);
        return chapter;
    }
}
