package se.yrgo.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import se.yrgo.exception.ProductNotFoundException;
import se.yrgo.exception.InvalidTransactionException;
import se.yrgo.service.CategoryService;
import se.yrgo.service.InventoryTransactionService;
import se.yrgo.service.ProductService;
import se.yrgo.service.SupplierService;

import java.util.Scanner;
import java.util.InputMismatchException;

public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);

    // 1) Load Spring context (scans for @Component, @Service, @PostConstruct, etc.)
    private static final ApplicationContext ctx =
            new ClassPathXmlApplicationContext("application.xml");

    // 2) Lookup by interface — works even if @Service has no explicit name
    private static final CategoryService categoryService =
            ctx.getBean(CategoryService.class);
    private static final ProductService productService =
            ctx.getBean(ProductService.class);
    private static final SupplierService supplierService =
            ctx.getBean(SupplierService.class);
    private static final InventoryTransactionService inventoryService =
            ctx.getBean(InventoryTransactionService.class);

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("\n=== Inventory Management System ===");
                System.out.println("1. Manage Categories");
                System.out.println("2. Manage Products");
                System.out.println("3. Manage Suppliers");
                System.out.println("4. Manage Inventory Transactions");
                System.out.println("5. Exit");
                System.out.print("Select an option: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> categoryMenu();
                    case "2" -> productMenu();
                    case "3" -> supplierMenu();
                    case "4" -> inventoryMenu();
                    case "5" -> {
                        System.out.println("Exiting. Goodbye!");
                        ((ClassPathXmlApplicationContext) ctx).close();
                        return;
                    }
                    default -> System.out.println("Invalid option. Please select 1-5.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    private static void categoryMenu() {
        while (true) {
            try {
                System.out.println("\n--- Category Menu ---");
                System.out.println("1. Create Category");
                System.out.println("2. List All Categories");
                System.out.println("3. Find Category by ID");
                System.out.println("4. Find Category by Name");
                System.out.println("5. Update Category");
                System.out.println("6. Delete Category");
                System.out.println("7. Delete All Categories");
                System.out.println("8. Back");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        try {
                            System.out.print("Enter category name: ");
                            String name = scanner.nextLine().trim();
                            if (name.isEmpty()) {
                                System.out.println("Category name cannot be empty.");
                                break;
                            }
                            categoryService.saveCategory(new Category(name));
                            System.out.println("Category created successfully.");
                        } catch (Exception e) {
                            System.err.println("Error creating category: " + e.getMessage());
                        }
                    }
                    case "2" -> {
                        try {
                            var categories = categoryService.getAllCategories();
                            if (categories.isEmpty()) {
                                System.out.println("No categories found.");
                            } else {
                                categories.forEach(System.out::println);
                            }
                        } catch (Exception e) {
                            System.err.println("Error retrieving categories: " + e.getMessage());
                        }
                    }
                    case "3" -> {
                        try {
                            System.out.print("Enter category ID: ");
                            long id = parseLong(scanner.nextLine());
                            Category category = categoryService.getCategoryById(id);
                            if (category != null) {
                                System.out.println(category);
                            } else {
                                System.out.println("Category not found with ID: " + id);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error finding category: " + e.getMessage());
                        }
                    }
                    case "4" -> {
                        try {
                            System.out.print("Enter category name: ");
                            String name = scanner.nextLine().trim();
                            if (name.isEmpty()) {
                                System.out.println("Category name cannot be empty.");
                                break;
                            }
                            Category category = categoryService.getCategoryByName(name);
                            if (category != null) {
                                System.out.println(category);
                            } else {
                                System.out.println("Category not found with name: " + name);
                            }
                        } catch (Exception e) {
                            System.err.println("Error finding category: " + e.getMessage());
                        }
                    }
                    case "5" -> {
                        try {
                            System.out.print("Enter category ID: ");
                            long id = parseLong(scanner.nextLine());
                            Category c = categoryService.getCategoryById(id);
                            if (c == null) {
                                System.out.println("Category not found with ID: " + id);
                                break;
                            }
                            System.out.print("Enter new name: ");
                            String newName = scanner.nextLine().trim();
                            if (newName.isEmpty()) {
                                System.out.println("Category name cannot be empty.");
                                break;
                            }
                            c.setName(newName);
                            categoryService.updateCategory(c);
                            System.out.println("Category updated successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error updating category: " + e.getMessage());
                        }
                    }
                    case "6" -> {
                        try {
                            System.out.print("Enter category ID to delete: ");
                            long id = parseLong(scanner.nextLine());
                            categoryService.deleteCategory(id);
                            System.out.println("Category deleted successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error deleting category: " + e.getMessage());
                        }
                    }
                    case "7" -> {
                        try {
                            System.out.print("Are you sure you want to delete all categories? (yes/no): ");
                            String confirm = scanner.nextLine().trim().toLowerCase();
                            if ("yes".equals(confirm)) {
                                categoryService.deleteAllCategories();
                                System.out.println("All categories deleted successfully.");
                            } else {
                                System.out.println("Operation cancelled.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error deleting categories: " + e.getMessage());
                        }
                    }
                    case "8" -> { return; }
                    default -> System.out.println("Invalid option. Please select 1-8.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void productMenu() throws ProductNotFoundException {
        while (true) {
            try {
                System.out.println("\n--- Product Menu ---");
                System.out.println("1. Create Product");
                System.out.println("2. List All Products");
                System.out.println("3. Find Product by ID");
                System.out.println("4. Find Product by Name");
                System.out.println("5. Update Product");
                System.out.println("6. Delete Product");
                System.out.println("7. List by Category");
                System.out.println("8. List by Supplier");
                System.out.println("9. Back");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        try {
                            // Basic fields
                            System.out.print("Enter product name: ");
                            String name = scanner.nextLine().trim();
                            if (name.isEmpty()) {
                                System.out.println("Product name cannot be empty.");
                                break;
                            }

                            System.out.print("Enter description: ");
                            String desc = scanner.nextLine().trim();

                            System.out.print("Enter price: ");
                            double price = parseDouble(scanner.nextLine());
                            if (price < 0) {
                                System.out.println("Price cannot be negative.");
                                break;
                            }

                            System.out.print("Enter quantity: ");
                            int quantity = parseInt(scanner.nextLine());
                            if (quantity < 0) {
                                System.out.println("Quantity cannot be negative.");
                                break;
                            }

                            System.out.print("Enter reorder level: ");
                            int reorderLevel = parseInt(scanner.nextLine());
                            if (reorderLevel < 0) {
                                System.out.println("Reorder level cannot be negative.");
                                break;
                            }

                            // Choose category
                            var categories = categoryService.getAllCategories();
                            if (categories.isEmpty()) {
                                System.out.println("No categories available. Please create a category first.");
                                break;
                            }
                            System.out.println("Available Categories:");
                            categories.forEach(c -> System.out.printf("  %d: %s%n", c.getId(), c.getName()));
                            System.out.print("Enter Category ID: ");
                            Long catId = parseLong(scanner.nextLine());
                            Category category = categoryService.getCategoryById(catId);
                            if (category == null) {
                                System.out.println("Category not found with ID: " + catId);
                                break;
                            }

                            // Choose supplier
                            var suppliers = supplierService.getAllSuppliers();
                            if (suppliers.isEmpty()) {
                                System.out.println("No suppliers available. Please create a supplier first.");
                                break;
                            }
                            System.out.println("Available Suppliers:");
                            suppliers.forEach(s -> System.out.printf("  %d: %s%n", s.getId(), s.getName()));
                            System.out.print("Enter Supplier ID: ");
                            Long supId = parseLong(scanner.nextLine());
                            Supplier supplier = supplierService.getSupplierById(supId);
                            if (supplier == null) {
                                System.out.println("Supplier not found with ID: " + supId);
                                break;
                            }

                            // Create and save product
                            Product p = new Product(name, desc, price, quantity, reorderLevel, category, supplier);
                            p.addSupplier(supplier);
                            productService.saveProduct(p);
                            System.out.println("Product created successfully: " + p);

                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format. Please enter valid numbers.");
                        } catch (Exception e) {
                            System.err.println("Error creating product: " + e.getMessage());
                        }
                    }
                    case "2" -> {
                        try {
                            var products = productService.getAllProducts();
                            if (products.isEmpty()) {
                                System.out.println("No products found.");
                            } else {
                                products.forEach(System.out::println);
                            }
                        } catch (Exception e) {
                            System.err.println("Error retrieving products: " + e.getMessage());
                        }
                    }
                    case "3" -> {
                        try {
                            System.out.print("Product ID: ");
                            long id = parseLong(scanner.nextLine());
                            Product product = productService.getProductById(id);
                            if (product != null) {
                                System.out.println(product);
                            } else {
                                System.out.println("Product not found with ID: " + id);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error finding product: " + e.getMessage());
                        }
                    }
                    case "4" -> {
                        try {
                            System.out.print("Product name: ");
                            String name = scanner.nextLine().trim();
                            if (name.isEmpty()) {
                                System.out.println("Product name cannot be empty.");
                                break;
                            }
                            Product product = productService.getProductByName(name);
                            if (product != null) {
                                System.out.println(product);
                            } else {
                                System.out.println("Product not found with name: " + name);
                            }
                        } catch (Exception e) {
                            System.err.println("Error finding product: " + e.getMessage());
                        }
                    }
                    case "5" -> {
                        try {
                            System.out.print("Product ID: ");
                            long pid = parseLong(scanner.nextLine());
                            Product p = productService.getProductById(pid);
                            if (p == null) {
                                System.out.println("Product not found with ID: " + pid);
                                break;
                            }
                            System.out.print("New name: ");
                            String newName = scanner.nextLine().trim();
                            if (newName.isEmpty()) {
                                System.out.println("Product name cannot be empty.");
                                break;
                            }
                            p.setName(newName);
                            productService.updateProduct(p);
                            System.out.println("Product updated successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error updating product: " + e.getMessage());
                        }
                    }
                    case "6" -> {
                        try {
                            System.out.print("Product ID to delete: ");
                            long id = parseLong(scanner.nextLine());
                            productService.deleteProduct(id);
                            System.out.println("Product deleted successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error deleting product: " + e.getMessage());
                        }
                    }
                    case "7" -> {
                        try {
                            System.out.print("Category ID: ");
                            long catId = parseLong(scanner.nextLine());
                            Category cat = categoryService.getCategoryById(catId);
                            if (cat == null) {
                                System.out.println("Category not found with ID: " + catId);
                                break;
                            }
                            var products = productService.getProductsByCategory(cat);
                            if (products.isEmpty()) {
                                System.out.println("No products found for category: " + cat.getName());
                            } else {
                                products.forEach(System.out::println);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error finding products by category: " + e.getMessage());
                        }
                    }
                    case "8" -> {
                        try {
                            System.out.print("Supplier ID: ");
                            long supId = parseLong(scanner.nextLine());
                            Supplier s = supplierService.getSupplierById(supId);
                            if (s == null) {
                                System.out.println("Supplier not found with ID: " + supId);
                                break;
                            }
                            var products = productService.getProductsBySupplier(s);
                            if (products.isEmpty()) {
                                System.out.println("No products found for supplier: " + s.getName());
                            } else {
                                products.forEach(System.out::println);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error finding products by supplier: " + e.getMessage());
                        }
                    }
                    case "9" -> { return; }
                    default -> System.out.println("Invalid option. Please select 1-9.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void supplierMenu() {
        while (true) {
            try {
                System.out.println("\n--- Supplier Menu ---");
                System.out.println("1. Create Supplier");
                System.out.println("2. List All Suppliers");
                System.out.println("3. Find Supplier by ID");
                System.out.println("4. Update Supplier");
                System.out.println("5. Delete Supplier");
                System.out.println("6. List Products for Supplier");
                System.out.println("7. Back");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        try {
                            System.out.print("Supplier name: ");
                            String name = scanner.nextLine().trim();
                            if (name.isEmpty()) {
                                System.out.println("Supplier name cannot be empty.");
                                break;
                            }
                            supplierService.saveSupplier(new Supplier(name));
                            System.out.println("Supplier created successfully.");
                        } catch (Exception e) {
                            System.err.println("Error creating supplier: " + e.getMessage());
                        }
                    }
                    case "2" -> {
                        try {
                            var suppliers = supplierService.getAllSuppliers();
                            if (suppliers.isEmpty()) {
                                System.out.println("No suppliers found.");
                            } else {
                                suppliers.forEach(System.out::println);
                            }
                        } catch (Exception e) {
                            System.err.println("Error retrieving suppliers: " + e.getMessage());
                        }
                    }
                    case "3" -> {
                        try {
                            System.out.print("Supplier ID: ");
                            long id = parseLong(scanner.nextLine());
                            Supplier supplier = supplierService.getSupplierById(id);
                            if (supplier != null) {
                                System.out.println(supplier);
                            } else {
                                System.out.println("Supplier not found with ID: " + id);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error finding supplier: " + e.getMessage());
                        }
                    }
                    case "4" -> {
                        try {
                            System.out.print("Supplier ID: ");
                            long id = parseLong(scanner.nextLine());
                            Supplier sup = supplierService.getSupplierById(id);
                            if (sup == null) {
                                System.out.println("Supplier not found with ID: " + id);
                                break;
                            }
                            System.out.print("New name: ");
                            String newName = scanner.nextLine().trim();
                            if (newName.isEmpty()) {
                                System.out.println("Supplier name cannot be empty.");
                                break;
                            }
                            sup.setName(newName);
                            supplierService.updateSupplier(sup);
                            System.out.println("Supplier updated successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error updating supplier: " + e.getMessage());
                        }
                    }
                    case "5" -> {
                        try {
                            System.out.print("Supplier ID to delete: ");
                            long id = parseLong(scanner.nextLine());
                            supplierService.deleteSupplierById(id);
                            System.out.println("Supplier deleted successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error deleting supplier: " + e.getMessage());
                        }
                    }
                    case "6" -> {
                        try {
                            System.out.print("Supplier ID: ");
                            long id = parseLong(scanner.nextLine());
                            var products = supplierService.getProductsForSupplier(id);
                            if (products.isEmpty()) {
                                System.out.println("No products found for supplier ID: " + id);
                            } else {
                                products.forEach(System.out::println);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (Exception e) {
                            System.err.println("Error finding products for supplier: " + e.getMessage());
                        }
                    }
                    case "7" -> { return; }
                    default -> System.out.println("Invalid option. Please select 1-7.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void inventoryMenu() throws InvalidTransactionException {
        while (true) {
            try {
                System.out.println("\n--- Inventory Transactions ---");
                System.out.println("1. Record Sale");
                System.out.println("2. Record Restock");
                System.out.println("3. Record Return");
                System.out.println("4. View Stock Level");
                System.out.println("5. List All Transactions");
                System.out.println("6. Back");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        try {
                            System.out.print("Product ID: ");
                            long pid = parseLong(scanner.nextLine());
                            System.out.print("Qty sold: ");
                            int q = parseInt(scanner.nextLine());
                            if (q <= 0) {
                                System.out.println("Quantity sold must be positive.");
                                break;
                            }
                            inventoryService.recordSale(pid, q);
                            System.out.println("Sale recorded successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format. Please enter valid numbers.");
                        } catch (ProductNotFoundException e) {
                            System.err.println(e.getMessage());
                        } catch (Exception e) {
                            System.err.println("Error recording sale: " + e.getMessage());
                        }
                    }
                    case "2" -> {
                        try {
                            System.out.print("Product ID: ");
                            long pid = parseLong(scanner.nextLine());
                            System.out.print("Qty restocked: ");
                            int q = parseInt(scanner.nextLine());
                            if (q <= 0) {
                                System.out.println("Quantity restocked must be positive.");
                                break;
                            }
                            inventoryService.recordRestock(pid, q);
                            System.out.println("Restock recorded successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format. Please enter valid numbers.");
                        } catch (ProductNotFoundException e) {
                            System.err.println(e.getMessage());
                        } catch (Exception e) {
                            System.err.println("Error recording restock: " + e.getMessage());
                        }
                    }
                    case "3" -> {
                        try {
                            System.out.print("Product ID: ");
                            long pid = parseLong(scanner.nextLine());
                            System.out.print("Qty returned: ");
                            int q = parseInt(scanner.nextLine());
                            if (q <= 0) {
                                System.out.println("Quantity returned must be positive.");
                                break;
                            }
                            inventoryService.recordReturn(pid, q);
                            System.out.println("Return recorded successfully.");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format. Please enter valid numbers.");
                        } catch (ProductNotFoundException e) {
                            System.err.println(e.getMessage());
                        } catch (Exception e) {
                            System.err.println("Error recording return: " + e.getMessage());
                        }
                    }
                    case "4" -> {
                        try {
                            System.out.print("Product ID: ");
                            long pid = parseLong(scanner.nextLine());
                            int stock = inventoryService.getProductStockLevel(pid);
                            System.out.println("Stock level: " + stock);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format. Please enter a valid number.");
                        } catch (ProductNotFoundException e) {
                            System.err.println(e.getMessage());
                        } catch (Exception e) {
                            System.err.println("Error retrieving stock level: " + e.getMessage());
                        }
                    }
                    case "5" -> {
                        try {
                            var transactions = inventoryService.getAllTransactions();
                            if (transactions.isEmpty()) {
                                System.out.println("No transactions found.");
                            } else {
                                transactions.forEach(System.out::println);
                            }
                        } catch (Exception e) {
                            System.err.println("Error retrieving transactions: " + e.getMessage());
                        }
                    }
                    case "6" -> { return; }
                    default -> System.out.println("Invalid option. Please select 1-6.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    // Helper methods for parsing with better error messages
    private static long parseLong(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("Input cannot be empty");
        }
        return Long.parseLong(input.trim());
    }

    private static int parseInt(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("Input cannot be empty");
        }
        return Integer.parseInt(input.trim());
    }

    private static double parseDouble(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("Input cannot be empty");
        }
        return Double.parseDouble(input.trim());
    }
}