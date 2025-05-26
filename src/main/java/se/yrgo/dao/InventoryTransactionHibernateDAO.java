package se.yrgo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.yrgo.entity.InventoryTransaction;

import java.util.List;

@Repository
public class InventoryTransactionHibernateDAO implements InventoryTransactionDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(InventoryTransaction transaction) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(transaction);
    }

    @Override
    public List<InventoryTransaction> findByProductId(Long productId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM InventoryTransaction WHERE product.id = :productId", InventoryTransaction.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    public List<InventoryTransaction> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT t FROM InventoryTransaction t JOIN FETCH t.product", InventoryTransaction.class)
                .getResultList();
    }

    @Override
    public void deleteAllTransaction() {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from InventoryTransaction").executeUpdate();
    }

    @Override
    public List<InventoryTransaction> findRecent(int count) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT t FROM InventoryTransaction t " +
                                "JOIN FETCH t.product " +
                                "ORDER BY t.timestamp DESC",
                        InventoryTransaction.class)
                .setMaxResults(count)
                .getResultList();
    }

}