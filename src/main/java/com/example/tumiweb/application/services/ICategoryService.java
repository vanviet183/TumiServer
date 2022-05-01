package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.CategoryDTO;
import com.example.tumiweb.domain.entity.Category;

import java.util.List;

public interface ICategoryService {

  List<Category> findAllCategory(Long page, int size, Boolean activeFlag, Boolean both);

  Category findCategoryById(Long id);

  Category createNewCategory(CategoryDTO categoryDTO);

  Category editCategoryById(Long id, CategoryDTO categoryDTO);

  Category deleteCategoryById(Long id);

  Category changeStatusById(Long id);

  Category save(Category category);

}
