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
        return entityManager.createQuery("FROM Supplier", Supplier.class).getResultList();
//        return findAllSuppliers();
    }

    @Override
    public Supplier getSupplierById(Long id) {
        List<Supplier> result = entityManager.createQuery(
                        "SELECT DISTINCT s FROM Supplier s LEFT JOIN FETCH s.products WHERE s.id = :id", Supplier.class)
                .setParameter("id", id)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

//    @Override
//    public List<Supplier> getAllSuppliers() {
//        return entityManager.createQuery("FROM Supplier", Supplier.class).getResultList();
//    }

    @Override
    public void deleteSupplierById(Long id) {
        Supplier supplier = entityManager.find(Supplier.class, id);
        if (supplier != null) {
            entityManager.remove(supplier);
        }
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        entityManager.merge(supplier);
    }

    @Override
    public void deleteAllSuppliers() {
        entityManager.createQuery("DELETE FROM Supplier").executeUpdate();
    }

}
