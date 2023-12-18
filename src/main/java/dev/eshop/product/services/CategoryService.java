package dev.eshop.product.services;

import dev.eshop.product.dtos.CategoryDto;
import dev.eshop.product.exceptions.CategoryNotFoundException;
import dev.eshop.product.models.Category;
import dev.eshop.product.models.Price;
import dev.eshop.product.models.Product;
import dev.eshop.product.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category getCategoryByName(String name) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findByNameIgnoreCase(name);
        if (category.isEmpty()){
            throw new CategoryNotFoundException("category not found");
        }

        return category.get();
    }

    public List<CategoryDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();

        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setUuid(category.getUuid());
            categoryDto.setName(category.getName());
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }


    public CategoryDto addCategory(String newCategory) {

        Optional<Category> optionalCategory = categoryRepository.findByNameIgnoreCase(newCategory);

        Category category;
        if (optionalCategory.isPresent()) {
            category = optionalCategory.get();
        } else {
            category = new Category();
            category.setName(newCategory);
            categoryRepository.save(category);
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setUuid(category.getUuid());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
