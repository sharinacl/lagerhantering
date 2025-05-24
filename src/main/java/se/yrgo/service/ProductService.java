package se.yrgo.service;

import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import java.util.List;

public interface ProductService {
    void saveProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(Category category);
    List<Product> getProductsBySupplier(Supplier supplier);
    void updateProduct(Product product);
    void deleteProduct(Long id);
    void updateInventory(Long productId, Integer quantity);
}
