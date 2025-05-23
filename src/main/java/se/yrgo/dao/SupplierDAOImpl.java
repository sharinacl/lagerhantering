package se.yrgo.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SupplierDAOImpl implements SupplierDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveSupplier(Supplier supplier) {
        Session session = entityManager.unwrap(Session.class);
        session.save(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Supplier.class, id);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("FROM Supplier", Supplier.class).getResultList();
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        Session session = entityManager.unwrap(Session.class);
        session.update(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Supplier supplier = session.get(Supplier.class, id);
        if (supplier != null) {
            session.delete(supplier);
        }
    }
}
