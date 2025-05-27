package se.yrgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dao.InventoryTransactionDAO;
import se.yrgo.dao.ProductDAO;
import se.yrgo.entity.InventoryTransaction;
import se.yrgo.entity.Product;
import se.yrgo.entity.TransactionType;
import se.yrgo.exception.ProductNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    @Autowired
    private InventoryTransactionDAO transactionDAO;

    @Autowired
    private ProductDAO productDAO;

    @Override
    @Transactional
    public void recordTransaction(Long productId, int quantity, TransactionType transactionType) throws ProductNotFoundException {
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
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        InventoryTransaction tx = new InventoryTransaction(-quantity, TransactionType.SALE, product);
        transactionDAO.save(tx);
        product.setQuantity(product.getQuantity() - quantity);
        productDAO.saveProduct(product);
    }

    @Override
    public void recordRestock(Long productId, int quantity) throws ProductNotFoundException {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        InventoryTransaction tx = new InventoryTransaction(-quantity, TransactionType.RESTOCK, product);
        transactionDAO.save(tx);
        product.setQuantity(product.getQuantity() + quantity);
        productDAO.saveProduct(product);
    }

    @Override
    public void recordReturn(Long productId, int quantity) throws ProductNotFoundException {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        InventoryTransaction tx = new InventoryTransaction(-quantity, TransactionType.RETURN, product);
        transactionDAO.save(tx);
        product.setQuantity(product.getQuantity() + quantity);
        productDAO.saveProduct(product);
    }


    @Override
    public int getProductStockLevel(Long productId) throws ProductNotFoundException {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        return transactionDAO.findByProductId(productId)
                .stream()
                .mapToInt(InventoryTransaction::getQuantity)
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransaction> getRecentTransactions(int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }
        return transactionDAO.findRecent(count);
    }

}
