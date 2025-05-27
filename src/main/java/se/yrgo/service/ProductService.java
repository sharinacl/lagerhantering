package se.yrgo.service;

import org.springframework.transaction.annotation.Transactional;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import se.yrgo.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    void saveProduct(Product product);
    Product getProductById(Long id) throws ProductNotFoundException;

    Product findByIdWithTransactionsAndSuppliers(Long id);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(Category category);
    List<Product> getProductsBySupplier(Supplier supplier);
    void updateProduct(Product product);
    void deleteProduct(Long id);
    void deleteAllProducts();
    void updateInventory(Long productId, Integer quantity);

    void restockProduct(String name, int qty);

    Product getProductByName(String name);
    List<Product> getLowStockProducts();


    void sellProduct(String name, int qty);

    // NEW METHODS FOR PRODUCT-SUPPLIER RELATIONSHIP MANAGEMENT
    void assignSupplierToProduct(Long productId, Long supplierId);
    void removeSupplierFromProduct(Long productId, Long supplierId);
    List<Supplier> getSuppliersForProduct(Long productId);
    boolean isProductSuppliedBy(Long productId, Long supplierId);
    int getSupplierCountForProduct(Long productId);
    List<Product> getProductsWithoutSuppliers();
    List<Product> getProductsWithMultipleSuppliers();

    // Business logic methods
    void establishSupplierRelationship(String productName, String supplierName);
    void terminateSupplierRelationship(String productName, String supplierName);
    List<String> getSupplierNamesForProduct(String productName);

//    @Transactional
//    Product findByIdWithTransactions(Long id);

}
