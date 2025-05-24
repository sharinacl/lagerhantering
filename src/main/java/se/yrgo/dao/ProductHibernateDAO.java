package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProductHibernateDAO implements ProductDAO {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(product);
    }

    @Override
    public Product getProductById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT p FROM Product p LEFT JOIN FETCH p.suppliers WHERE p.id = :id", Product.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public Product findByIdWithTransactions(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT p FROM Product p LEFT JOIN FETCH p.transactions WHERE p.id = :id",
                        Product.class
                )
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Product> getAllProducts() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Product", Product.class).getResultList();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Product p JOIN p.category c WHERE c = :category", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Product> getProductsBySupplier(Supplier supplier) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Product p WHERE p.suppliers = :supplier", Product.class)
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

    @Override
    public void deleteAllProducts() {

    }


    @Override
    public void updateInventory(Long productId, Integer quantity) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, productId);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            session.update(product);
        }
    }

    @Override
    public Product getProductByName(String name) {
        List<Product> results = em.createQuery("FROM Product WHERE name = :name", Product.class)
                .setParameter("name", name)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Product> getLowStockProducts() {
        return em.createQuery("FROM Product WHERE inventoryQuantity < reorderLevel", Product.class).getResultList();
    }

}