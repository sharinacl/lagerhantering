package se.yrgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dao.CategoryDAO;
import se.yrgo.entity.Category;

import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    @Transactional
    public void saveCategory(Category category) {
        categoryDAO.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryDAO.delete(id);
    }

    @Override
    @Transactional
    public List<Category> getAllCategoriesWithProducts() {
        return categoryDAO.getAllCategoriesWithProducts();
    }

    @Override
    @Transactional
    public Category getCategoryByName(String name) {
        return categoryDAO.getCategoryByName(name);
    }

    @Override
    public void updateCategory(Category category) {
        categoryDAO.save(category);
    }

    @Override
    public void deleteAllCategories() {
        categoryDAO.deleteAllCategories();
    }

}
