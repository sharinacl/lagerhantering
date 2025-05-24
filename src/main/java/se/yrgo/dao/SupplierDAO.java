package se.yrgo.dao;

import se.yrgo.entity.Supplier;

import java.util.List;

public interface SupplierDAO {
    void saveSupplier(Supplier supplier);
    Supplier getSupplierById(Long id);
    List<Supplier> getAllSuppliers();
    void updateSupplier(Supplier supplier);
    void deleteSupplier(Long id);
}
