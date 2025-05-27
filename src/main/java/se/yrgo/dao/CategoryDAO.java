package se.yrgo.dao;

import se.yrgo.entity.Category;

import java.util.List;

public interface CategoryDAO {
    Category getCategoryByName(String name);
    void save(Category category);
    List<Category> findAll();
    Category findById(Long id);
    void delete(Long id);
    List<Category> getAllCategoriesWithProducts();
    void deleteAllCategories();

}
