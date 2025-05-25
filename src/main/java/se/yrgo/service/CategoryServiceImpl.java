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
    public void save(Category category) {
        categoryDAO.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryDAO.findById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryDAO.delete(id);
    }

}
