package se.yrgo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.entity.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveSupplier(Supplier supplier) {
        entityManager.persist(supplier);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
//        return entityManager.createQuery("FROM Supplier", Supplier.class).getResultList();
        return findAllSuppliers();
    }

    @Override
    public Supplier getSupplierById(int id) {
        return entityManager.find(Supplier.class, id);
    }

    @Override
    public List<Supplier> findAllSuppliers() {
        return entityManager.createQuery("FROM Supplier", Supplier.class).getResultList();
    }

    @Override
    public void deleteSupplierById(int id) {
        Supplier supplier = entityManager.find(Supplier.class, id);
        if (supplier != null) {
            entityManager.remove(supplier);
        }
    }

    @Override
    public void updateSupplier(Supplier supplier) {

    }

    @Override
    public void deleteAllSuppliers() {

    }

}
