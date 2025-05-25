package se.yrgo.service;

import org.springframework.transaction.annotation.Transactional;
import se.yrgo.entity.InventoryTransaction;
import se.yrgo.entity.Product;
import se.yrgo.entity.TransactionType;
import se.yrgo.exception.InvalidTransactionException;
import se.yrgo.exception.ProductNotFoundException;

import java.util.List;

public interface InventoryTransactionService {
    // Core operations
    void recordTransaction(Long productId, int quantity, TransactionType type)
            throws ProductNotFoundException, InvalidTransactionException;

    // Query methods
    List<InventoryTransaction> getTransactionsForProduct(Long productId);
    List<InventoryTransaction> getAllTransactions();
    List<InventoryTransaction> getTransactionsByType(TransactionType type);

    // Specific transaction operations
    void recordSale(Long productId, int quantity) throws ProductNotFoundException;
    void recordRestock(Long productId, int quantity) throws ProductNotFoundException;
    void recordReturn(Long productId, int quantity) throws ProductNotFoundException;
    void recordAdjustment(Long productId, int quantity, String notes) throws ProductNotFoundException;

    // Business intelligence
    int getProductStockLevel(Long productId) throws ProductNotFoundException;
    List<InventoryTransaction> getRecentTransactions(int count);

    @Transactional
    void saveTransaction(InventoryTransaction inventoryTransaction);

    @Transactional
    void recordSale(Product product, int quantity);

    @Transactional
    void recordRestock(Product product, int quantity);

    @Transactional
    void deleteAllTransactions();

}