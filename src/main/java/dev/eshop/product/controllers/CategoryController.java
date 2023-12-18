package dev.eshop.product.controllers;

import dev.eshop.product.dtos.CategoryDto;
import dev.eshop.product.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity getAllCategories(){
        List<CategoryDto> allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(allCategories);
    }

    @PostMapping
    public ResponseEntity addCategory(@RequestBody String newCategory){
        CategoryDto addCategoryResponse = categoryService.addCategory(newCategory);
        return ResponseEntity.ok(addCategoryResponse);
    }
}
