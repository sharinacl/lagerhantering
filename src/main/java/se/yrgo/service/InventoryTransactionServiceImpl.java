package se.yrgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dao.InventoryTransactionDAO;
import se.yrgo.dao.ProductDAO;
import se.yrgo.entity.InventoryTransaction;
import se.yrgo.entity.Product;
import se.yrgo.entity.TransactionType;
import se.yrgo.exception.InvalidTransactionException;
import se.yrgo.exception.ProductNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    @Autowired
    private InventoryTransactionDAO transactionDAO;

    @Autowired
    private ProductDAO productDAO;

    @Override
    @Transactional
    public void recordTransaction(Long productId, int quantity, TransactionType transactionType) {
        Product product = productDAO.getProductById(productId);
        if (product == null) throw new IllegalArgumentException("Invalid product ID");

        InventoryTransaction tx = new InventoryTransaction(quantity, product);
        tx.setTimestamp(LocalDateTime.now());
        transactionDAO.save(tx);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransaction> getTransactionsForProduct(Long productId) {
        return transactionDAO.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransaction> getAllTransactions() {
        return transactionDAO.findAll();
    }

    @Override
    public List<InventoryTransaction> getTransactionsByType(TransactionType type) {
        return List.of();
    }

    @Override
    public void recordSale(Long productId, int quantity) throws ProductNotFoundException {

    }

    @Override
    public void recordRestock(Long productId, int quantity) throws ProductNotFoundException {

    }

    @Override
    public void recordReturn(Long productId, int quantity) throws ProductNotFoundException {

    }

    @Override
    public void recordAdjustment(Long productId, int quantity, String notes) throws ProductNotFoundException {

    }

    @Override
    public int getProductStockLevel(Long productId) throws ProductNotFoundException {
        return 0;
    }

    @Override
    public List<InventoryTransaction> getRecentTransactions(int count) {
        return List.of();
    }

    @Transactional
    @Override
    public void saveTransaction(InventoryTransaction inventoryTransaction) {
        inventoryTransaction.setTimestamp(LocalDateTime.now());
        transactionDAO.save(inventoryTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public void recordSale(Product product, int quantity) {
        InventoryTransaction tx = new InventoryTransaction(quantity, TransactionType.SALE, product);
        tx.setTimestamp(LocalDateTime.now());
        transactionDAO.save(tx);
    }

    @Transactional
    @Override
    public void recordRestock(Product product, int quantity) {
        InventoryTransaction tx = new InventoryTransaction(quantity, TransactionType.RESTOCK, product);
        tx.setTimestamp(LocalDateTime.now());
        transactionDAO.save(tx);
    }

    @Override
    public void deleteAllTransactions() {
        transactionDAO.deleteAllTransaction();
    }

}
