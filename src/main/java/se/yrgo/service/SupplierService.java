package se.yrgo.service;

import se.yrgo.entity.Supplier;

import java.util.List;

public interface SupplierService {

    void saveSupplier(Supplier supplier);

    List<Supplier> getAllSuppliers();

    Supplier getSupplierById(int id);

    List<Supplier> findAllSuppliers();

    void deleteSupplierById(int id);

    void updateSupplier(Supplier supplier);

    void deleteAllSuppliers();

}
