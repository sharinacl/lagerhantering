package se.yrgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.yrgo.entity.Supplier;
import se.yrgo.service.SupplierService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return supplier != null ?
                ResponseEntity.ok(supplier) :
                ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return ResponseEntity
                .created(URI.create("/suppliers/" + supplier.getId()))
                .body(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable Long id,
            @RequestBody Supplier updated) {
        Supplier existing = supplierService.getSupplierById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        updated.setId(id);
        supplierService.updateSupplier(updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        Supplier existing = supplierService.getSupplierById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
