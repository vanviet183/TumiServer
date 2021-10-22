package com.example.tumiweb.repository;

import com.example.tumiweb.dao.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Set<Chapter> findAllByStatus(boolean status);
    Set<Chapter> findAllByCourse_Id(Long courseId);
}
