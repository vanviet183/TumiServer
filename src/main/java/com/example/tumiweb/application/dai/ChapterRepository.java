package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

  Set<Chapter> findAllByDeleteFlag(boolean flag);

  Set<Chapter> findAllByCourse_Id(Long courseId);

}
