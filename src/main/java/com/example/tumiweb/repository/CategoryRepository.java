package com.example.tumiweb.repository;

import com.example.tumiweb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    Set<Category> findAllByStatus(boolean status);
}
