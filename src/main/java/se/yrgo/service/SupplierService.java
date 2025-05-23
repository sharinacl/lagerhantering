package se.yrgo.service;

import se.yrgo.entity.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> getAllSuppliers();
    Supplier getSupplierById(Long id);
    void saveSupplier(Supplier supplier);
    void updateSupplier(Supplier supplier);
    void deleteSupplier(Long id);
}
