package se.yrgo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.Category;

import java.util.List;

@Repository
public class CategoryHibernateDAO implements CategoryDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Category category) {
        Session session = em.unwrap(Session.class);
        session.saveOrUpdate(category);
    }

    @Override
    public List<Category> findAll() {
        Session session = em.unwrap(Session.class);
        return session.createQuery("from Category", Category.class).getResultList();
    }

    @Override
    public Category findById(Long id) {
        Session session = em.unwrap(Session.class);
        return session.get(Category.class, id);
    }

    @Override
    public void delete(Long id) {
        Session session = em.unwrap(Session.class);
        Category categoryName = session.get(Category.class, id);
        if (categoryName != null) {
            session.delete(categoryName);
        }
    }

}
