package se.yrgo.service;

import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import java.util.List;

public interface SupplierService {

    void saveSupplier(Supplier supplier);

    List<Supplier> getAllSuppliers();

    Supplier getSupplierById(Long id);

//    List<Supplier> findAllSuppliers();

    void deleteSupplierById(Long id);

    void updateSupplier(Supplier supplier);

    void deleteAllSuppliers();

    // NEW METHODS FOR SUPPLIER-PRODUCT RELATIONSHIP MANAGEMENT
    void assignProductToSupplier(Long supplierId, Long productId);
    void removeProductFromSupplier(Long supplierId, Long productId);
    List<Product> getProductsForSupplier(Long supplierId);
    boolean doesSupplierSupplyProduct(Long supplierId, Long productId);
    int getProductCountForSupplier(Long supplierId);
    double getTotalProductValueForSupplier(Long supplierId);
    List<Supplier> getSuppliersWithoutProducts();
    List<Supplier> getTopSuppliersByProductCount(int limit);


    // Business logic methods
    void establishProductRelationship(String supplierName, String productName);
    void terminateProductRelationship(String supplierName, String productName);
    List<String> getProductNamesForSupplier(String supplierName);

}
