package com.example.api_petopia.service;

import com.example.api_petopia.entity.Category;
import com.example.api_petopia.entity.Product;
import com.example.api_petopia.repository.CategoryRepository;
import com.example.api_petopia.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deletedById(Long id) {
        categoryRepository.deleteById(id);
    }
}
