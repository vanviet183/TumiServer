package com.example.tumiweb.repository;

import com.example.tumiweb.dao.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByTitle(String title);
    Image findAllByPath(String path);
}
