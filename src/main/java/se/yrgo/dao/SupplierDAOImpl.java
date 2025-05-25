package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.Supplier;

import java.util.List;

@Repository
public class SupplierDAOImpl implements SupplierDAO {

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