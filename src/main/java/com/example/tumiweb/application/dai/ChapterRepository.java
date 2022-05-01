package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

  List<Chapter> findAllByDeleteFlag(boolean flag);

  List<Chapter> findAllByCourse_IdAndActiveFlag(Long courseId, Boolean activeFlag);

}
