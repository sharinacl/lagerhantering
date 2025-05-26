package se.yrgo.dao;

import se.yrgo.entity.InventoryTransaction;
import java.util.List;

public interface InventoryTransactionDAO {
    void save(InventoryTransaction tx);
    List<InventoryTransaction> findByProductId(Long productId);

    List<InventoryTransaction> findAll();

    void deleteAllTransaction();
    /**
     * Returns the most recent inventory transactions, newest first.
     *
     * @param count max number of transactions to return
     * @return up to {@code count} transactions ordered by timestamp descending
     */
    List<InventoryTransaction> findRecent(int count);

}
