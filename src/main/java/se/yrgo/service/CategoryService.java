package se.yrgo.service;

import org.springframework.transaction.annotation.Transactional;
import se.yrgo.entity.Category;

import java.util.List;

public interface CategoryService {

    @Transactional
    void saveCategory(Category category);

    @Transactional
    List<Category> getAllCategories();

    @Transactional(readOnly = true)
    Category getCategoryById(Long id);

    @Transactional
    void deleteCategory(Long id);

    List<Category> getAllCategoriesWithProducts();
    Category getCategoryByName(String categoryName);

    void updateCategory(Category category);

    void deleteAllCategories();

}
