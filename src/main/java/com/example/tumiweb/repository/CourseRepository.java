package com.example.tumiweb.repository;

import com.example.tumiweb.dao.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String name);

    List<Course> findAllByNameContainingOrDescriptionContaining(String key, String key2);
}
