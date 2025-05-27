package se.yrgo.app;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import se.yrgo.exception.ProductNotFoundException;
import se.yrgo.service.CategoryService;
import se.yrgo.service.ProductService;
import se.yrgo.service.SupplierService;
import se.yrgo.service.InventoryTransactionService;

import java.util.Collections;
import java.util.Set;

@Component
public class MockDataInitializer {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryTransactionService inventoryService;

    @PostConstruct
    public void initData() throws ProductNotFoundException {
        // --- Create & save categories ---
        Category electronics = new Category("Electronics", "Electronic gadgets and devices");
        Category groceries   = new Category("Groceries",   "Food and everyday consumables");
        categoryService.saveCategory(electronics);
        categoryService.saveCategory(groceries);

        // --- Create & save suppliers ---
        Supplier acme    = new Supplier("Acme Electronics");
        Supplier foodies = new Supplier("Foodies Market");
        supplierService.saveSupplier(acme);
        supplierService.saveSupplier(foodies);

        // --- Create & save products, each with a Set<Supplier> ---
        Product tv = new Product("Smart TV", "42-inch 4K Smart TV", 399.99, 20,5,electronics, acme);
        tv.setSuppliers(Collections.singleton(acme));                  // ← now a Set<Supplier>
        productService.saveProduct(tv);

        Product apple = new Product("Apple", "Fresh Red Apple", 0.39, 30, 25, groceries, foodies);
        apple.setSuppliers(Collections.singleton(foodies));
        productService.saveProduct(apple);

        // --- Seed some inventory transactions via your service layer ---
        inventoryService.recordRestock(tv.getId(), 100);
        inventoryService.recordSale   (tv.getId(),   1);
        inventoryService.recordRestock(apple.getId(),200);
        inventoryService.recordSale   (apple.getId(), 30);
    }
}
