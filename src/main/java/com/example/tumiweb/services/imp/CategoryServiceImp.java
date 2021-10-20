package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.CategoryDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.model.Category;
import com.example.tumiweb.repository.CategoryRepository;
import com.example.tumiweb.services.ICategoryService;
import com.github.slugify.Slugify;
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

    @Autowired
    private Slugify slugify;

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
        }else if(status){ //lấy 1 kiểu active
            if(page == null) {
                //true and no paging
                categories = categoryRepository.findAllByStatus(true);
            }else {
                //true and paging
                categories = categoryRepository.findAllByStatus(true);
                int length = categories.size();
                int totalPage = (length % page != 0) ? length/size + 1 : length/size;
                if(totalPage > page.intValue()) {
                    return new HashSet<>();
                }
                categories = new HashSet<>(new ArrayList<>(categories).subList(page.intValue()*size, page.intValue()*size + size));
            }
        }else {
            categories = new HashSet<>(categoryRepository.findAll());
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
        if(category != null) {
            throw new DuplicateException("Duplicate Category by name: " + categoryDTO.getName());
        }

        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        newCategory.setSeo(slugify.slugify(newCategory.getName()));
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category editCategoryById(Long id, CategoryDTO categoryDTO) {
        Category category = findCategoryById(id);
        if(category == null) {
            throw new NotFoundException("Can not find Category by id: " + id);
        }
        
        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        newCategory.setSeo(slugify.slugify(newCategory.getName()));
        return categoryRepository.save(newCategory);
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
    public Category changeStatusById(Long id) {
        Category category = findCategoryById(id);
        if(category == null) {
            throw new NotFoundException("Can not find Category by id: " + id);
        }
        category.setStatus(!category.getStatus());
        return categoryRepository.save(category);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }


}
