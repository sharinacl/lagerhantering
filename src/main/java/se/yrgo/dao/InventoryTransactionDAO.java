package se.yrgo.dao;

import se.yrgo.entity.InventoryTransaction;
import java.util.List;

public interface InventoryTransactionDAO {
    void save(InventoryTransaction tx);
    List<InventoryTransaction> findByProductId(Long productId);

    List<InventoryTransaction> findAll();

    void deleteAllTransaction();

}
