package se.yrgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dao.ProductDAO;
import se.yrgo.dao.SupplierDAO;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import se.yrgo.exception.ProductNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SupplierDAO supplierDAO;

    @Override
    @Transactional
    public void saveProduct(Product product) {
        productDAO.saveProduct(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) throws ProductNotFoundException {
        Product p = productDAO.getProductById(id);
        if (p != null) {
            p.getSuppliers().size();
            if (p.getCategory() != null) {
                p.getCategory().getName();
            }
        }
        return p;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findByIdWithTransactionsAndSuppliers(Long id) {
        return productDAO.findByIdWithTransactionsAndSuppliers(id);
    }


    @Override
    @Transactional (readOnly = true)
    public List<Product> getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        products.forEach(p -> {
            // touch the collection
            p.getSuppliers().size();
            // touch the category
            if (p.getCategory() != null) {
                p.getCategory().getName();
            }
        });
        return products;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Product> getProductsByCategory(Category category) {
        List<Product> products = productDAO.getProductsByCategory(category);
        for (Product product : products) {
            product.getSuppliers().size(); // This triggers lazy loading
        }
        return products;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Product> getProductsBySupplier(Supplier supplier) {
        List<Product> products = productDAO.getProductsBySupplier(supplier);
        // Force initialization of lazy collections
        for (Product product : products) {
            product.getSuppliers().size();
        }
        return products;
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productDAO.deleteProduct(id);
    }

    @Override
    @Transactional
    public void deleteAllProducts() {
        productDAO.deleteAllProducts();
    }

    @Override
    @Transactional
    public void updateInventory(Long productId, Integer quantity) {
        if (quantity==0) {
            throw new IllegalArgumentException("Quantity change cannot be zero");
        }
        productDAO.updateInventory(productId, quantity);
    }

    @Override
    @Transactional
    public void restockProduct(String name, int quantity) {
        Product p = productDAO.getProductByName(name);  // Make sure this method exists in DAO
        if (p == null) {
            throw new IllegalArgumentException("Product not found: " + name);
        }

        p.setQuantity(p.getQuantity() + quantity);
        productDAO.updateProduct(p);  // Persist the changes
    }

    @Override
    @Transactional
    public Product getProductByName(String name) {
        Product p = productDAO.getProductByName(name);
        if (p != null) {
            p.getSuppliers().size();
            if (p.getCategory() != null) {
                p.getCategory().getName();
            }
        }
        return p;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        return productDAO.getLowStockProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public void sellProduct(String name, int qty) {
        Product product = productDAO.getProductByName(name);
        if (product == null) {
            throw new IllegalArgumentException("❌ Product not found: " + name);
        }

        if (product.getQuantity() < qty) {
            throw new IllegalArgumentException("❌ Not enough stock for: " + name);
        }

        product.setQuantity(product.getQuantity() - qty);
        productDAO.updateProduct(product);
    }

    // NEW IMPLEMENTATION METHODS FOR PRODUCT-SUPPLIER RELATIONSHIPS
    @Override
    @Transactional
    public void assignSupplierToProduct(Long productId, Long supplierId) {
        productDAO.addSupplierToProduct(productId, supplierId);
    }

    @Override
    @Transactional
    public void removeSupplierFromProduct(Long productId, Long supplierId) {
        productDAO.removeSupplierFromProduct(productId, supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersForProduct(Long productId) {

        return productDAO.getSuppliersByProduct(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductSuppliedBy(Long productId, Long supplierId) {
        return productDAO.isProductSuppliedBy(productId, supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getSupplierCountForProduct(Long productId) {
        return productDAO.getSupplierCountForProduct(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsWithoutSuppliers() {
        return productDAO.getProductsWithoutSuppliers();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsWithMultipleSuppliers() {
        return productDAO.getProductsWithMultipleSuppliers();
    }

    // BUSINESS LOGIC METHODS
    @Override
    @Transactional
    public void establishSupplierRelationship(String productName, String supplierName) {
        Product product = productDAO.getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productName);
        }

        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        Supplier supplier = suppliers.stream()
                .filter(s -> s.getName().equalsIgnoreCase(supplierName))
                .findFirst()
                .orElse(null);

        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found: " + supplierName);
        }

        productDAO.addSupplierToProduct(product.getId(), supplier.getId());
    }

    @Override
    @Transactional
    public void terminateSupplierRelationship(String productName, String supplierName) {
        Product product = productDAO.getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productName);
        }

        Supplier supplier = product.findSupplierByName(supplierName);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier relationship not found: " + supplierName);
        }

        productDAO.removeSupplierFromProduct(product.getId(), supplier.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSupplierNamesForProduct(String productName) {
        Product product = productDAO.getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productName);
        }

        return productDAO.getSuppliersByProduct(product.getId())
                .stream()
                .map(Supplier::getName)
                .collect(Collectors.toList());
    }

}
