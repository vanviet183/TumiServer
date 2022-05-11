package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.ICategoryService;
import com.example.tumiweb.domain.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class CategoryController {
  private final ICategoryService categoryService;

  public CategoryController(ICategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping(UrlConstant.Category.DATA_CATEGORY)
  public ResponseEntity<?> getAllCategories(@RequestParam(name = "page", required = false) Long page,
                                            @RequestParam(name = "status", required = false) boolean status,
                                            @RequestParam(name = "both", required = false) boolean both) {
    return VsResponseUtil.ok(categoryService.findAllCategory(page, CommonConstant.SIZE_OFF_PAGE, status, both));
  }

  @PostMapping(UrlConstant.Category.DATA_CATEGORY)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<?> createNewCategory(@RequestBody CategoryDTO categoryDTO) {
    return VsResponseUtil.ok(categoryService.createNewCategory(categoryDTO));
  }

  @PatchMapping(UrlConstant.Category.DATA_CATEGORY_ID)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<?> editCategoryById(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) {
    return VsResponseUtil.ok(categoryService.editCategoryById(id, categoryDTO));
  }

  @GetMapping(UrlConstant.Category.DATA_CATEGORY_ID)
  public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(categoryService.findCategoryById(id));
  }

  @PostMapping(UrlConstant.Category.DATA_CATEGORY_STATUS)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<?> changeStatusById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(categoryService.changeStatusById(id));
  }

  @DeleteMapping(UrlConstant.Category.DATA_CATEGORY_ID)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(categoryService.deleteCategoryById(id));
  }

}
