package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
import com.example.tumiweb.application.dai.CategoryRepository;
import com.example.tumiweb.application.mapper.CategoryMapper;
import com.example.tumiweb.application.services.ICategoryService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.CategoryDTO;
import com.example.tumiweb.domain.entity.Category;
import com.example.tumiweb.domain.entity.base.AbstractAuditingEntity;
import com.github.slugify.Slugify;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  public List<Category> findAllCategory(Long page, int size, Boolean activeFlag, Boolean both) {
    List<Category> categories;
    if (page != null) {
      categories = categoryRepository.findAll(PageRequest.of(page.intValue(), size)).getContent();
    } else {
      categories = categoryRepository.findAll();
    }

    if (both) {
      return categories;
    }

    if (activeFlag) {
      return categories.parallelStream().filter(AbstractAuditingEntity::getActiveFlag).collect(Collectors.toList());
    }
    return categories;
  }

  //  @Cacheable(value = "category", key = "#id")
  @Override
  public Category findCategoryById(Long id) {
    Optional<Category> category = categoryRepository.findById(id);
    if (category.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "category", id));
    }
    if (category.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DELETE, id));
    }
    return category.get();
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category createNewCategory(CategoryDTO categoryDTO) {
    Category category = categoryRepository.findByName(categoryDTO.getName());
    if (category != null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DUPLICATE_NAME, categoryDTO.getName()));
    }

    Category newCategory = categoryMapper.toCategory(categoryDTO);
    newCategory.setSeo(slugify.slugify(newCategory.getName()));
    return categoryRepository.save(newCategory);
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category editCategoryById(Long id, CategoryDTO categoryDTO) {
    Category category = findCategoryById(id);

    category.setName(categoryDTO.getName());
    category.setDescription(categoryDTO.getDescription());
    category.setSeo(slugify.slugify(category.getName()));

    return categoryRepository.save(category);
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category deleteCategoryById(Long id) {
    Category category = findCategoryById(id);

    category.setDeleteFlag(true);

    return categoryRepository.save(category);
  }

  //  @CacheEvict(value = "category", allEntries = true)
  @Override
  public Category changeStatusById(Long id) {
    Category category = findCategoryById(id);

    category.setDeleteFlag(!category.getDeleteFlag());

    return categoryRepository.save(category);
  }

  @Override
  public Category save(Category category) {
    return categoryRepository.save(category);
  }

}
