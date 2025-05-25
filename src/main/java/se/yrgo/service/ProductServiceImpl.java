package se.yrgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dao.ProductDAO;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    @Transactional
    public void saveProduct(Product product) {
        productDAO.saveProduct(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productDAO.getProductById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findByIdWithTransactionsAndSuppliers(Long id) {
        return productDAO.findByIdWithTransactionsAndSuppliers(id);
    }


    @Override
    @Transactional (readOnly = true)
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @Override
    @Transactional (readOnly = true)
    public List<Product> getProductsByCategory(Category category) {
        return productDAO.getProductsByCategory(category);
    }

    @Override
    @Transactional (readOnly = true)
    public List<Product> getProductsBySupplier(Supplier supplier) {
        return productDAO.getProductsBySupplier(supplier);
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productDAO.deleteProduct(id);
    }

    @Override
    public void deleteAllProducts() {
        productDAO.deleteAllProducts();
    }

    @Override
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
    public Product getProductByName(String name) {
        return productDAO.getProductByName(name);
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

    @Override
    @Transactional
    public Product findByIdWithTransactions(Long id) {
        return productDAO.findByIdWithTransactions(id);
    }
}
