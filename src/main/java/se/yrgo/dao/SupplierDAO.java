package se.yrgo.dao;

import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import java.util.List;

public interface SupplierDAO {
    void saveSupplier(Supplier supplier);
    Supplier getSupplierById(Long id);
    List<Supplier> getAllSuppliers();
    void updateSupplier(Supplier supplier);
    void deleteSupplier(Long id);
    void deleteAllSuppliers();
    void addProductToSupplier(Long supplierId, Long productId);
    void removeProductFromSupplier(Long supplierId, Long productId);
    List<Product> getProductsBySupplier(Long supplierId);
    boolean doesSupplierSupplyProduct(Long supplierId, Long productId);
    int getProductCountForSupplier(Long supplierId);
    double getTotalProductValueForSupplier(Long supplierId);
    List<Supplier> getSuppliersWithoutProducts();
    List<Supplier> getSuppliersWithMostProducts(int limit);
    List<String> getProductNamesBySupplier(Long supplierId);
    Supplier getSupplierByName(String supplierName);

}
