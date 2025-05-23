package se.yrgo.service;

import org.springframework.transaction.annotation.Transactional;
import se.yrgo.entity.Category;

import java.util.List;

public interface CategoryService {

    @Transactional
    void save(Category category);

    @Transactional
    List<Category> getAll();

    @Transactional(readOnly = true)
    Category getById(Long id);

    @Transactional
    void delete(Long id);

}
