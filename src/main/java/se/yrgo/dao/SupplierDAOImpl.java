package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import se.yrgo.entity.Supplier;

import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {
    private SessionFactory sessionFactory;

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
}
