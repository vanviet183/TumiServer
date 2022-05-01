package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.CategoryRepository;
import com.example.tumiweb.application.mapper.CategoryMapper;
import com.example.tumiweb.application.services.ICategoryService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.CategoryDTO;
import com.example.tumiweb.domain.entity.Category;
import com.github.slugify.Slugify;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryServiceImp implements ICategoryService {
  private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
  private final CategoryRepository categoryRepository;
  private final Slugify slugify;

  public CategoryServiceImp(CategoryRepository categoryRepository, Slugify slugify) {
    this.categoryRepository = categoryRepository;
    this.slugify = slugify;
  }

  //  @Cacheable(value = "category", key = "'all'")
  @Override
  public Set<Category> findAllCategory(Long page, int size, boolean status, boolean both) {
    Set<Category> categories = new HashSet<>();
    //lấy cả 2 true and false
    if (both) {
      if (page == null) {
        categories = new HashSet<>(categoryRepository.findAll());
      } else {
        categories = new HashSet<>(categoryRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
      }
    } else if (status) { //lấy 1 kiểu active
      if (page == null) {
        //true and no paging
        categories = categoryRepository.findAllByDeleteFlag(true);
      } else {
        //true and paging
        categories = categoryRepository.findAllByDeleteFlag(true);
        int length = categories.size();
        int totalPage = (length % page != 0) ? length / size + 1 : length / size;
        if (totalPage > page.intValue()) {
          return new HashSet<>();
        }
        categories = new HashSet<>(new ArrayList<>(categories).subList(page.intValue() * size,
            page.intValue() * size + size));
      }
    } else {
      categories = new HashSet<>(categoryRepository.findAll());
    }
    return categories;
  }

  //  @Cacheable(value = "category", key = "#id")
  @Override
  public Category findCategoryById(Long id) {
    Optional<Category> category = categoryRepository.findById(id);
    if (category.isEmpty()) {
      throw new VsException("Can not find Category by id: " + id);
    }
    return category.get();
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category createNewCategory(CategoryDTO categoryDTO) {
    Category category = categoryRepository.findByName(categoryDTO.getName());
    if (category != null) {
      throw new VsException("Duplicate Category by name: " + categoryDTO.getName());
    }

    Category newCategory = categoryMapper.toCategory(categoryDTO);
    newCategory.setSeo(slugify.slugify(newCategory.getName()));
    return categoryRepository.save(newCategory);
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category editCategoryById(Long id, CategoryDTO categoryDTO) {
    Category category = findCategoryById(id);
    if (category == null) {
      throw new VsException("Can not find Category by id: " + id);
    }

    Category newCategory = categoryMapper.toCategory(categoryDTO);
    newCategory.setSeo(slugify.slugify(newCategory.getName()));
    return categoryRepository.save(newCategory);
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category deleteCategoryById(Long id) {
    Category category = findCategoryById(id);
    if (category == null) {
      throw new VsException("Can not find Category by id: " + id);
    }
    categoryRepository.delete(category);
    return category;
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category changeStatusById(Long id) {
    Category category = findCategoryById(id);
    if (category == null) {
      throw new VsException("Can not find Category by id: " + id);
    }
    category.setDeleteFlag(!category.getDeleteFlag());
    return categoryRepository.save(category);
  }

  @Override
  public Category save(Category category) {
    return categoryRepository.save(category);
  }

}
