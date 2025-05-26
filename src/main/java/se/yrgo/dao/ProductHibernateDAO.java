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
    public Product findByIdWithTransactionsAndSuppliers(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT DISTINCT p FROM Product p " +
                                "LEFT JOIN FETCH p.suppliers " +
                                "LEFT JOIN FETCH p.transactions " +
                                "WHERE p.id = :id", Product.class)
                .setParameter("id", id)
                .uniqueResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Product> getAllProducts() {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "select distinct p " +
                                "from Product p " +
                                "left join fetch p.category " +
                                "left join fetch p.suppliers"
                )
                .getResultList();
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
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from Product").executeUpdate();
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

    // NEW IMPLEMENTATION METHODS FOR PRODUCT-SUPPLIER RELATIONSHIPS
    @Override
    public void addSupplierToProduct(Long productId, Long supplierId) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, productId);
        Supplier supplier = session.get(Supplier.class, supplierId);

        if (product != null && supplier != null) {
            product.addSupplier(supplier); // Uses the method we created in Product entity
            session.update(product);
        }
    }

    @Override
    public void removeSupplierFromProduct(Long productId, Long supplierId) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, productId);
        Supplier supplier = session.get(Supplier.class, supplierId);

        if (product != null && supplier != null) {
            product.removeSupplier(supplier); // Uses the method we created in Product entity
            session.update(product);
        }
    }

    @Override
    public List<Supplier> getSuppliersByProduct(Long productId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT s FROM Supplier s JOIN s.products p WHERE p.id = :productId", Supplier.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    public boolean isProductSuppliedBy(Long productId, Long supplierId) {
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(
                        "SELECT COUNT(*) FROM Product p JOIN p.suppliers s WHERE p.id = :productId AND s.id = :supplierId", Long.class)
                .setParameter("productId", productId)
                .setParameter("supplierId", supplierId)
                .uniqueResult();
        return count > 0;
    }

    @Override
    public int getSupplierCountForProduct(Long productId) {
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(
                        "SELECT COUNT(s) FROM Product p JOIN p.suppliers s WHERE p.id = :productId", Long.class)
                .setParameter("productId", productId)
                .uniqueResult();
        return count.intValue();
    }

    @Override
    public List<Product> getProductsWithoutSuppliers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT p FROM Product p WHERE p.suppliers IS EMPTY", Product.class)
                .getResultList();
    }

    @Override
    public List<Product> getProductsWithMultipleSuppliers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT p FROM Product p WHERE SIZE(p.suppliers) > 1", Product.class)
                .getResultList();
    }

}