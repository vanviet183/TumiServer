package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  Image findByTitle(String title);

  Image findAllByPath(String path);

}
