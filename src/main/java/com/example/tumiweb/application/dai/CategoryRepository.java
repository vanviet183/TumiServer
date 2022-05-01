package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  Category findByName(String name);

  List<Category> findAllByDeleteFlag(Boolean deleteFlag);

}
