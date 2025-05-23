package se.yrgo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import se.yrgo.entity.Supplier;

import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {
    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void saveSupplier(Supplier supplier) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.persist(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Supplier.class, id);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.createQuery("FROM Supplier", Supplier.class).getResultList();
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.merge(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Supplier supplier = entityManager.find(Supplier.class, id);
        if (supplier != null) {
            entityManager.remove(supplier);
        }
    }
}
