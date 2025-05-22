package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

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
        return session.createQuery("FROM Product", Product.class).getResultList();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Product p WHERE p.category =:category", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Product> getProductsBySupplier(Supplier supplier) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Product p WHERE p.supplier = :supplier", Product.class)
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
        if(product != null) {
            session.delete(product);
        }
    }

    @Override
    public void updateInventory(Long productId, Integer quantity) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, productId);
        if(product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            session.update(product);
        }
    }
}
