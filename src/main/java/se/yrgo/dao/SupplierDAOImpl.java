package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SupplierDAOImpl implements SupplierDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveSupplier(Supplier supplier) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT DISTINCT s FROM Supplier s LEFT JOIN FETCH s.products WHERE s.id = :id", Supplier.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Supplier", Supplier.class).getResultList();
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        Session session = sessionFactory.getCurrentSession();
        session.update(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.get(Supplier.class, id);
        if (supplier != null) {
            session.delete(supplier);
        }
    }

    @Override
    public void deleteAllSuppliers() {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from Supplier").executeUpdate();
    }

    @Override
    public void addProductToSupplier(Long supplierId, Long productId) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.get(Supplier.class, supplierId);
        Product product = session.get(Product.class, productId);

        if (supplier != null && product != null) {
            supplier.addProduct(product); // Uses the method we created in Supplier entity
            session.update(supplier);
        }
    }

    @Override
    public void removeProductFromSupplier(Long supplierId, Long productId) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.get(Supplier.class, supplierId);
        Product product = session.get(Product.class, productId);

        if (supplier != null && product != null) {
            supplier.removeProduct(product); // Uses the method we created in Supplier entity
            session.update(supplier);
        }
    }

    @Override
    public List<Product> getProductsBySupplier(Long supplierId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT p FROM Product p JOIN p.suppliers s WHERE s.id = :supplierId", Product.class)
                .setParameter("supplierId", supplierId)
                .getResultList();
    }

    @Override
    public boolean doesSupplierSupplyProduct(Long supplierId, Long productId) {
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(
                        "SELECT COUNT(*) FROM Supplier s JOIN s.products p WHERE s.id = :supplierId AND p.id = :productId", Long.class)
                .setParameter("supplierId", supplierId)
                .setParameter("productId", productId)
                .uniqueResult();
        return count > 0;
    }

    @Override
    public int getProductCountForSupplier(Long supplierId) {
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(
                        "SELECT COUNT(p) FROM Supplier s JOIN s.products p WHERE s.id = :supplierId", Long.class)
                .setParameter("supplierId", supplierId)
                .uniqueResult();
        return count.intValue();
    }

    @Override
    public double getTotalProductValueForSupplier(Long supplierId) {
        Session session = sessionFactory.getCurrentSession();
        Double total = session.createQuery(
                        "SELECT SUM(p.price) FROM Supplier s JOIN s.products p WHERE s.id = :supplierId", Double.class)
                .setParameter("supplierId", supplierId)
                .uniqueResult();
        return total != null ? total : 0.0;
    }

    @Override
    public List<Supplier> getSuppliersWithoutProducts() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT s FROM Supplier s WHERE s.products IS EMPTY", Supplier.class)
                .getResultList();
    }

    @Override
    public List<Supplier> getSuppliersWithMostProducts(int limit) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT s FROM Supplier s ORDER BY SIZE(s.products) DESC", Supplier.class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Supplier getSupplierByName(String supplierName) {
        List<Supplier> supplierResult = entityManager.createQuery("from Supplier where name = :name", Supplier.class)
                .setParameter("name", supplierName)
                .getResultList();
        return supplierResult.size() > 0 ? supplierResult.get(0) : null;
    }

}