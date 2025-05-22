package se.yrgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dao.ProductDAO;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import java.util.List;

@Service
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
    public void updateInventory(Long productId, Integer quantity) {
        productDAO.updateInventory(productId, quantity);
    }
}
