package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.Supplier;

import java.util.List;

@Repository
public class SupplierHibernateDAO implements SupplierDAO {
    private SessionFactory sessionFactory;

    // Setter for Spring to inject the SessionFactory
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveSupplier(Supplier supplier) {
        Session session = sessionFactory.getCurrentSession();
        session.save(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Supplier.class, id);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Supplier", Supplier.class)
                .getResultList();
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

    // Example of additional helper method: remove a specific product from supplier
    public void removeProductFromSupplier(Long supplierId, Long productId) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.get(Supplier.class, supplierId);
        if (supplier != null) {
            supplier.getProducts().removeIf(p -> p.getId().equals(productId));
            session.update(supplier);
        }
    }
}
