package se.yrgo.service;

import org.springframework.transaction.annotation.Transactional;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

import java.util.List;

public interface ProductService {
    void saveProduct(Product product);
    Product getProductById(Long id);

    Product findByIdWithTransactionsAndSuppliers(Long id);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(Category category);
    List<Product> getProductsBySupplier(Supplier supplier);
    void updateProduct(Product product);
    void deleteProduct(Long id);
    void deleteAllProducts();
    void updateInventory(Long productId, Integer quantity);

    void restockProduct(String name, int qty);

    Product getProductByName(String name);
    List<Product> getLowStockProducts();


    void sellProduct(String name, int qty);

//    @Transactional
//    Product findByIdWithTransactions(Long id);

}
