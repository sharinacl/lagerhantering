package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.Category;

import java.util.List;

@Repository
public class CategoryHibernateDAO implements CategoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Category getCategoryByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Category.class, name);
    }

    @Override
    public void save(Category category) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(category);
    }

    @Override
    public List<Category> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.products", Category.class)
                .getResultList();
    }

    @Override
    public Category findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id", Category.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Category category = session.get(Category.class, id);
        if (category != null) {
            session.delete(category);
        }
    }

    @Override
    public List<Category> getAllCategoriesWithProducts() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.products", Category.class)
                .getResultList();
    }

    @Override
    public void deleteAllCategories() {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from Category").executeUpdate();

    }

}