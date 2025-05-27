package se.yrgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dao.ProductDAO;
import se.yrgo.dao.SupplierDAO;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private SupplierDAO supplierDAO;

    @Override
    public void saveSupplier(Supplier supplier) {
        supplierDAO.saveSupplier(supplier);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierDAO.getSupplierById(id);
    }


    @Override
    public void deleteSupplierById(Long id) {
        if (supplierDAO.getSupplierById(id) == null) {
            throw new IllegalArgumentException("Supplier with id " + id + " not found");
        }
        for (Product product : supplierDAO.getProductsBySupplier(id)) {
            product.getSuppliers().remove(supplierDAO.getSupplierById(id));
        }
        supplierDAO.getProductsBySupplier(id).clear();
        supplierDAO.deleteSupplier(id);
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        supplierDAO.updateSupplier(supplier);
    }

    @Override
    public void deleteAllSuppliers() {
        supplierDAO.deleteAllSuppliers();
    }

    @Override
    public void assignProductToSupplier(Long supplierId, Long productId) {
        supplierDAO.addProductToSupplier(productId, supplierId);
    }

    @Override
    public void removeProductFromSupplier(Long supplierId, Long productId) {
        supplierDAO.removeProductFromSupplier(productId, supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsForSupplier(Long supplierId) {
        return supplierDAO.getProductsBySupplier(supplierId);
    }

    @Override
    public boolean doesSupplierSupplyProduct(Long supplierId, Long productId) {
        return supplierDAO.doesSupplierSupplyProduct(productId, supplierId);
    }

    @Override
    public int getProductCountForSupplier(Long supplierId) {
        return supplierDAO.getProductCountForSupplier(supplierId);
    }

    @Override
    public double getTotalProductValueForSupplier(Long supplierId) {
        return supplierDAO.getTotalProductValueForSupplier(supplierId);
    }

    @Override
    public List<Supplier> getSuppliersWithoutProducts() {
        return supplierDAO.getSuppliersWithoutProducts();
    }

    @Override
    public List<Supplier> getTopSuppliersByProductCount(int limit) {
        return supplierDAO.getSuppliersWithMostProducts(limit);
    }

    @Override
    public void establishProductRelationship(String supplierName, String productName) {
        Supplier supplier = supplierDAO.getSupplierByName(supplierName);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier " + supplierName + " not found");
        }

        List<Product> products = productDAO.getAllProducts();
        Product product = products.stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst().orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found");
        }

    }

    @Override
    public void terminateProductRelationship(String supplierName, String productName) {
        Supplier supplier = supplierDAO.getSupplierByName(supplierName);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier " + supplierName + " not found");
        }

        Product product = productDAO.getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product relationship " + productName + " not found");
        }

        supplierDAO.removeProductFromSupplier(product.getId(), supplier.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getProductNamesForSupplier(String supplierName) {
        Supplier supplier = supplierDAO.getSupplierByName(supplierName);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier " + supplierName + " not found");
        }
        return supplierDAO.getProductNamesBySupplier(supplier.getId());
    }

}
