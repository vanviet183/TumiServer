package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  Course findByName(String name);

  List<Course> findAllByNameContainingOrDescriptionContaining(String key, String key2);

}
