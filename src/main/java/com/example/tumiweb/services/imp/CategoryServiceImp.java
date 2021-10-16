package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.CategoryDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Category;
import com.example.tumiweb.repository.CategoryRepository;
import com.example.tumiweb.services.ICategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryServiceImp implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Set<Category> findAllCategory(Long page, int size, boolean status, boolean both) {
        Set<Category> categories = new HashSet<>();
        //lấy cả 2 true and false
        if(both) {
            if(page == null) {
                categories = new HashSet<>(categoryRepository.findAll());
            }else {
                categories = new HashSet<>(categoryRepository.findAll(PageRequest.of(page.intValue(), size)).getContent());
            }
        }else { //lấy 1 kiểu active
            if(status && page == null) {
                //true and no paging
                categories = categoryRepository.findAllByStatus(true);
            }else if(status) {
                //true and paging
                categories = categoryRepository.findAllByStatus(true);
                int length = categories.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                categories = new HashSet<>(new ArrayList<>(categories).subList(page.intValue()*size, page.intValue()*size + size));
            }else if(page == null) {
                //false and no paging
                categories = categoryRepository.findAllByStatus(false);
            }else {
                //false and paging
                categories = categoryRepository.findAllByStatus(false);
                int length = categories.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                categories = new HashSet<>(new ArrayList<>(categories).subList(page.intValue()*size, page.intValue()*size + size));
            }
        }
        return categories;
    }

    @Override
    public Category findCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new NotFoundException("Can not find Category by id: " + id);
        }
        return category.get();
    }

    @Override
    public Category createNewCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findByName(categoryDTO.getName());
        if(category == null) {
            throw new DuplicateException("Duplicate Category by name: " + categoryDTO.getName());
        }
        return categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
    }

    @Override
    public Category editCategoryById(Long id, CategoryDTO categoryDTO) {
        Category category = findCategoryById(id);
        if(category == null) {
            throw new NotFoundException("Can not find Category by id: " + id);
        }
        return categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
    }

    @Override
    public Category deleteCategoryById(Long id) {
        Category category = findCategoryById(id);
        if(category == null) {
            throw new NotFoundException("Can not find Category by id: " + id);
        }
        categoryRepository.delete(category);
        return category;
    }

    @Override
    public Category changeStatusById(Long id, boolean status) {
        Category category = findCategoryById(id);
        if(category == null) {
            throw new NotFoundException("Can not find Category by id: " + id);
        }
        category.setStatus(status);
        return categoryRepository.save(category);
    }
}
