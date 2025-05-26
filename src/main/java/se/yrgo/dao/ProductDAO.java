package se.yrgo.dao;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import java.util.List;

public interface ProductDAO {
    void saveProduct(Product product);
    Product getProductById(Long id);

    Product findByIdWithTransactions(Long id);

    Product findByIdWithTransactionsAndSuppliers(Long id);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(Category category);
    List<Product> getProductsBySupplier(Supplier supplier);
    void deleteProduct(Long id);
    void deleteAllProducts();
    void updateInventory(Long productId, Integer quantity);

    Product getProductByName(String name);
    void updateProduct(Product product);

    List<Product> getLowStockProducts();
    void addSupplierToProduct(Long productId, Long supplierId);
    void removeSupplierFromProduct(Long productId, Long supplierId);
    List<Supplier> getSuppliersByProduct(Long productId);
    boolean isProductSuppliedBy(Long productId, Long supplierId);
    int getSupplierCountForProduct(Long productId);
    List<Product> getProductsWithoutSuppliers();
    List<Product> getProductsWithMultipleSuppliers();

}
