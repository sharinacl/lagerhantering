package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import java.util.List;

public class ProductHibernateDAO implements ProductDAO {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Product.class, id);
    }

    @Override
    public List<Product> getAllProducts() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("From Product p Where p.category = :category", Product.class).list();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("From Product p Where p.category = :category", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Product> getProductsBySupplier(Supplier supplier) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("From Product p Where p.category = :category", Product.class)
                .setParameter("supplier", supplier)
                .getResultList();
    }

    @Override
    public void updateProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.update(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, id);
        if (product != null) {
            session.delete(product);
        }
    }

    public void updateInventory(Long productid, Integer quantity) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, productid);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            session.update(product);
        }
    }

}
