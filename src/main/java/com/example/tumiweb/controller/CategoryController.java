package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.CategoryDTO;
import com.example.tumiweb.dao.Category;
import com.example.tumiweb.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController<Category> {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getAllCategories(
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "status", required = false) boolean status,
            @RequestParam(name = "both", required = false) boolean both
    ) {
        return this.resSetSuccess(categoryService.findAllCategory(page, Constants.SIZE_OFF_PAGE, status, both));
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createNewCategory(@RequestBody CategoryDTO categoryDTO) {
        return this.resSuccess(categoryService.createNewCategory(categoryDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> editCategoryById(
            @PathVariable("id") Long id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        return this.resSuccess(categoryService.editCategoryById(id, categoryDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        return this.resSuccess(categoryService.findCategoryById(id));
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> changeStatusById(@PathVariable("id") Long id) {
        return this.resSuccess(categoryService.changeStatusById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Long id) {
        return this.resSuccess(categoryService.deleteCategoryById(id));
    }

}
