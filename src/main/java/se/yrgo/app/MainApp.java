package se.yrgo.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.entity.*;
import se.yrgo.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainApp {
    private static ProductService productService;
    private static SupplierService supplierService;
    private static CategoryService categoryService;
    private static InventoryTransactionService transactionService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        productService = context.getBean(ProductService.class);
        supplierService = context.getBean(SupplierService.class);
        categoryService = context.getBean(CategoryService.class);
        transactionService = context.getBean(InventoryTransactionService.class);

        // Initialize with mock data
        new MockDataInitializer(context).initializeAllData();

        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getIntInput("Select option: ");

            switch (choice) {
                case 1:
                    productMenu();
                    break;
                case 2:
                    categoryMenu();
                    break;
                case 3:
                    supplierMenu();
                    break;
                case 4:
                    transactionMenu();
                    break;
                case 5:
                    displayInventoryReport();
                    break;
                case 6:
                    running = false;
                    System.out.println("Shutting down inventory management system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n========================================");
        System.out.println("    INVENTORY MANAGEMENT SYSTEM - MAIN MENU");
        System.out.println("========================================");
        System.out.println("1. Product Management (CRUD)");
        System.out.println("2. Category Management (CRUD)");
        System.out.println("3. Supplier Management (CRUD)");
        System.out.println("4. Inventory Transactions");
        System.out.println("5. Inventory Report");
        System.out.println("6. Exit");
        System.out.println("========================================");
    }

    // PRODUCT CRUD OPERATIONS
    private static void productMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- PRODUCT MANAGEMENT ---");
            System.out.println("1. View all products");
            System.out.println("2. Find product by ID");
            System.out.println("3. Add new product");
            System.out.println("4. Update product");
            System.out.println("5. Delete product");
            System.out.println("6. Back to main menu");

            int choice = getIntInput("Select option: ");

            switch (choice) {
                case 1:
                    viewAllProducts();
                    break;
                case 2:
                    findProductById();
                    break;
                case 3:
                    createProduct();
                    break;
                case 4:
                    updateProduct();
                    break;
                case 5:
                    deleteProduct();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllProducts() {
        System.out.println("\n=== ALL PRODUCTS ===");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.printf("%-5s %-20s %-10s %-8s %-15s %-10s%n",
                    "ID", "Name", "Price", "Qty", "Category", "Reorder Lvl");
            System.out.println("-".repeat(80));
            for (Product p : products) {
                System.out.printf("%-5d %-20s %-10.2f %-8d %-15s %-10d%n",
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getQuantity(),
                        p.getCategory() != null ? p.getCategory().getName() : "N/A",
                        p.getReorderLevel());
            }
        }
    }

    private static void findProductById() {
        long id = getLongInput("Enter product ID: ");
        Product product = productService.findByIdWithTransactionsAndSuppliers(id);
        if (product != null) {
            displayProductDetails(product);
        } else {
            System.out.println("Product with ID " + id + " not found.");
        }
    }

    private static void createProduct() {
        System.out.println("\n=== ADD NEW PRODUCT ===");
        String name = getStringInput("Product name: ");
        String description = getStringInput("Description: ");
        double price = getDoubleInput("Price: ");
        int quantity = getIntInput("Quantity: ");
        int reorderLevel = getIntInput("Reorder level: ");

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setReorderLevel(reorderLevel);

        // Optional: Add category
        System.out.println("Available categories:");
        List<Category> categories = categoryService.getAllCategories();
        for (Category cat : categories) {
            System.out.println(cat.getId() + ". " + cat.getName());
        }

        long categoryId = getLongInput("Select category ID (0 for none): ");
        if (categoryId > 0) {
            Category category = categoryService.getCategoryById(categoryId);
            if (category != null) {
                product.setCategory(category);
            }
        }

        productService.saveProduct(product);
        System.out.println("Product created with ID: " + product.getId());
    }

    private static void updateProduct() {
        long id = getLongInput("Enter ID of product to update: ");
        Product product = productService.findByIdWithTransactionsAndSuppliers(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Current product information:");
        displayProductDetails(product);

        System.out.println("\nEnter new values (press Enter to keep current value):");

        String newName = getStringInput("New name [" + product.getName() + "]: ");
        if (!newName.trim().isEmpty()) {
            product.setName(newName);
        }

        String newDesc = getStringInput("New description [" + product.getDescription() + "]: ");
        if (!newDesc.trim().isEmpty()) {
            product.setDescription(newDesc);
        }

        String priceStr = getStringInput("New price [" + product.getPrice() + "]: ");
        if (!priceStr.trim().isEmpty()) {
            product.setPrice(Double.parseDouble(priceStr));
        }

        String qtyStr = getStringInput("New quantity [" + product.getQuantity() + "]: ");
        if (!qtyStr.trim().isEmpty()) {
            product.setQuantity(Integer.parseInt(qtyStr));
        }

        productService.updateProduct(product);
        System.out.println("Product updated.");
    }

    private static void deleteProduct() {
        long id = getLongInput("Enter ID of product to delete: ");
        Product product = productService.findByIdWithTransactionsAndSuppliers(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Product to delete:");
        displayProductDetails(product);

        String confirm = getStringInput("Are you sure you want to delete this product? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            productService.deleteProduct(id);
            System.out.println("Product deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // CATEGORY CRUD OPERATIONS
    private static void categoryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- CATEGORY MANAGEMENT ---");
            System.out.println("1. View all categories");
            System.out.println("2. Find category by ID");
            System.out.println("3. Add new category");
            System.out.println("4. Update category");
            System.out.println("5. Delete category");
            System.out.println("6. Back to main menu");

            int choice = getIntInput("Select option: ");

            switch (choice) {
                case 1:
                    viewAllCategories();
                    break;
                case 2:
                    findCategoryById();
                    break;
                case 3:
                    createCategory();
                    break;
                case 4:
                    updateCategory();
                    break;
                case 5:
                    deleteCategory();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllCategories() {
        System.out.println("\n=== ALL CATEGORIES ===");
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.printf("%-5s %-20s %-30s %-10s%n", "ID", "Name", "Description", "Products");
            System.out.println("-".repeat(75));
            for (Category c : categories) {
                System.out.printf("%-5d %-20s %-30s %-10d%n",
                        c.getId(),
                        c.getName(),
                        c.getDescription() != null ? c.getDescription() : "N/A",
                        c.getProducts().size());
            }
        }
    }

    private static void findCategoryById() {
        long id = getLongInput("Enter category ID: ");
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            displayCategoryDetails(category);
        } else {
            System.out.println("Category with ID " + id + " not found.");
        }
    }

    private static void createCategory() {
        System.out.println("\n=== ADD NEW CATEGORY ===");
        String name = getStringInput("Category name: ");
        String description = getStringInput("Description: ");

        Category category = new Category(name, description);
        categoryService.saveCategory(category);
        System.out.println("Category created with ID: " + category.getId());
    }

    private static void updateCategory() {
        long id = getLongInput("Enter ID of category to update: ");
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            System.out.println("Category not found.");
            return;
        }

        System.out.println("Current category information:");
        displayCategoryDetails(category);

        System.out.println("\nEnter new values (press Enter to keep current value):");

        String newName = getStringInput("New name [" + category.getName() + "]: ");
        if (!newName.trim().isEmpty()) {
            category.setName(newName);
        }

        String newDesc = getStringInput("New description [" + category.getDescription() + "]: ");
        if (!newDesc.trim().isEmpty()) {
            category.setDescription(newDesc);
        }

        categoryService.updateCategory(category);
        System.out.println("Category updated.");
    }

    private static void deleteCategory() {
        long id = getLongInput("Enter ID of category to delete: ");
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            System.out.println("Category not found.");
            return;
        }

        if (!category.getProducts().isEmpty()) {
            System.out.println("Cannot delete category containing products (" +
                    category.getProducts().size() + " products).");
            return;
        }

        System.out.println("Category to delete:");
        displayCategoryDetails(category);

        String confirm = getStringInput("Are you sure you want to delete this category? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            categoryService.deleteCategory(id);
            System.out.println("Category deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // SUPPLIER CRUD OPERATIONS
    private static void supplierMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- SUPPLIER MANAGEMENT ---");
            System.out.println("1. View all suppliers");
            System.out.println("2. Find supplier by ID");
            System.out.println("3. Add new supplier");
            System.out.println("4. Update supplier");
            System.out.println("5. Delete supplier");
            System.out.println("6. Back to main menu");

            int choice = getIntInput("Select option: ");

            switch (choice) {
                case 1:
                    viewAllSuppliers();
                    break;
                case 2:
                    getSupplierById();
                    break;
                case 3:
                    createSupplier();
                    break;
                case 4:
                    updateSupplier();
                    break;
                case 5:
                    deleteSupplier();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllSuppliers() {
        System.out.println("\n=== ALL SUPPLIERS ===");
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
        } else {
            System.out.printf("%-5s %-20s %-20s %-25s %-15s%n",
                    "ID", "Company", "Contact", "Email", "Phone");
            System.out.println("-".repeat(90));
            for (Supplier s : suppliers) {
                System.out.printf("%-5d %-20s %-20s %-25s %-15s%n",
                        s.getId(),
                        s.getName() != null ? s.getName() : "N/A",
                        s.getContactName() != null ? s.getContactName() : "N/A",
                        s.getEmail() != null ? s.getEmail() : "N/A",
                        s.getPhone() != null ? s.getPhone() : "N/A");
            }
        }
    }

    private static void getSupplierById() {
        Long id = (long) getIntInput("Enter supplier ID: ");
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier != null) {
            displaySupplierDetails(supplier);
        } else {
            System.out.println("Supplier with ID " + id + " not found.");
        }
    }

    private static void createSupplier() {
        System.out.println("\n=== ADD NEW SUPPLIER ===");
        String name = getStringInput("Company name: ");
        String contactName = getStringInput("Contact person: ");
        String email = getStringInput("Email: ");
        String phone = getStringInput("Phone: ");
        String address = getStringInput("Address: ");

        Supplier supplier = new Supplier(name, contactName, email, phone, address);
        supplierService.saveSupplier(supplier);
        System.out.println("Supplier created with ID: " + supplier.getId());
    }

    private static void updateSupplier() {
        Long id = (long) getIntInput("Enter ID of supplier to update: ");
        Supplier supplier = supplierService.getSupplierById(id);

        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.println("Current supplier information:");
        displaySupplierDetails(supplier);

        System.out.println("\nEnter new values (press Enter to keep current value):");

        String newName = getStringInput("New company name [" + supplier.getName() + "]: ");
        if (!newName.trim().isEmpty()) {
            supplier.setName(newName);
        }

        String newContact = getStringInput("New contact person [" + supplier.getContactName() + "]: ");
        if (!newContact.trim().isEmpty()) {
            supplier.setContactName(newContact);
        }

        String newEmail = getStringInput("New email [" + supplier.getEmail() + "]: ");
        if (!newEmail.trim().isEmpty()) {
            supplier.setEmail(newEmail);
        }

        String newPhone = getStringInput("New phone number [" + supplier.getPhone() + "]: ");
        if (!newPhone.trim().isEmpty()) {
            supplier.setPhone(newPhone);
        }

        String newAddress = getStringInput("New address [" + supplier.getAddress() + "]: ");
        if (!newAddress.trim().isEmpty()) {
            supplier.setAddress(newAddress);
        }

        supplierService.updateSupplier(supplier);
        System.out.println("Supplier updated.");
    }

    private static void deleteSupplier() {
        Long id = (long) getIntInput("Enter ID of supplier to delete: ");
        Supplier supplier = supplierService.getSupplierById(id);

        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.println("Supplier to delete:");
        displaySupplierDetails(supplier);

        String confirm = getStringInput("Are you sure you want to delete this supplier? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            supplierService.deleteSupplierById(id);
            System.out.println("Supplier deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // TRANSACTION OPERATIONS
    private static void transactionMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- INVENTORY TRANSACTIONS ---");
            System.out.println("1. View all transactions");
            System.out.println("2. Record sale");
            System.out.println("3. Record restock");
            System.out.println("4. View transactions for product");
            System.out.println("5. Back to main menu");

            int choice = getIntInput("Select option: ");

            switch (choice) {
                case 1:
                    viewAllTransactions();
                    break;
                case 2:
                    recordSale();
                    break;
                case 3:
                    recordRestock();
                    break;
                case 4:
                    viewTransactionsForProduct();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllTransactions() {
        System.out.println("\n=== ALL TRANSACTIONS ===");
        List<InventoryTransaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.printf("%-5s %-20s %-10s %-8s %-20s%n",
                    "ID", "Product", "Type", "Qty", "Date/Time");
            System.out.println("-".repeat(70));
            for (InventoryTransaction t : transactions) {
                System.out.printf("%-5d %-20s %-10s %-8d %-20s%n",
                        t.getId(),
                        t.getProduct() != null ? t.getProduct().getName() : "N/A",
                        t.getType(),
                        t.getQuantity(),
                        t.getTimestamp());
            }
        }
    }

    private static void recordSale() {
        System.out.println("\n=== RECORD SALE ===");
        long productId = getLongInput("Product ID: ");
        Product product = productService.getProductById(productId);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Selected product: " + product.getName() + " (Available: " + product.getQuantity() + ")");
        int quantity = getIntInput("Quantity sold: ");

        if (quantity > product.getQuantity()) {
            System.out.println("Insufficient stock! Available: " + product.getQuantity());
            return;
        }

        InventoryTransaction transaction = new InventoryTransaction(-quantity, TransactionType.SALE, product);
        transactionService.saveTransaction(transaction);

        // Update product quantity
        product.setQuantity(product.getQuantity() - quantity);
        productService.updateProduct(product);

        System.out.println("Sale recorded. New stock level: " + product.getQuantity());
    }

    private static void recordRestock() {
        System.out.println("\n=== RECORD RESTOCK ===");
        long productId = getLongInput("Product ID: ");
        Product product = productService.getProductById(productId);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Selected product: " + product.getName() + " (Current stock: " + product.getQuantity() + ")");
        int quantity = getIntInput("Restock quantity: ");

        InventoryTransaction transaction = new InventoryTransaction(quantity, TransactionType.RESTOCK, product);
        transactionService.saveTransaction(transaction);

        // Update product quantity
        product.setQuantity(product.getQuantity() + quantity);
        productService.updateProduct(product);

        System.out.println("Restock recorded. New stock level: " + product.getQuantity());
    }

    private static void viewTransactionsForProduct() {
        long productId = getLongInput("Enter product ID: ");
        Product product = productService.getProductById(productId);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("\n=== TRANSACTIONS FOR " + product.getName().toUpperCase() + " ===");
        List<InventoryTransaction> transactions = product.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this product.");
        } else {
            System.out.printf("%-10s %-8s %-20s%n", "Type", "Qty", "Date/Time");
            System.out.println("-".repeat(45));
            for (InventoryTransaction t : transactions) {
                System.out.printf("%-10s %-8d %-20s%n",
                        t.getType(),
                        t.getQuantity(),
                        t.getTimestamp());
            }
        }
    }

    // INVENTORY REPORT
    private static void displayInventoryReport() {
        System.out.println("\n========== INVENTORY REPORT ==========");

        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.getAllCategoriesWithProducts();
        List<Supplier> suppliers = supplierService.getAllSuppliers();

        System.out.println("Total products: " + products.size());
        System.out.println("Total categories: " + categories.size());
        System.out.println("Total suppliers: " + suppliers.size());

        System.out.println("\n--- LOW STOCK PRODUCTS ---");
        boolean lowStockFound = false;
        for (Product p : products) {
            if (p.getQuantity() <= p.getReorderLevel()) {
                if (!lowStockFound) {
                    System.out.printf("%-20s %-8s %-15s%n", "Product", "Qty", "Reorder Level");
                    System.out.println("-".repeat(50));
                    lowStockFound = true;
                }
                System.out.printf("%-20s %-8d %-15d%n", p.getName(), p.getQuantity(), p.getReorderLevel());
            }
        }
        if (!lowStockFound) {
            System.out.println("No products with low stock.");
        }

        System.out.println("\n--- CATEGORIES AND PRODUCT COUNTS ---");
        if (!categories.isEmpty()) {
            System.out.printf("%-20s %-10s%n", "Category", "Products");
            System.out.println("-".repeat(35));
            for (Category c : categories) {
                System.out.printf("%-20s %-10d%n", c.getName(), c.getProducts().size());
            }
        }

        System.out.println("==================================");
    }

    // UTILITY METHODS
    private static void displayProductDetails(Product product) {
        System.out.println("\n--- PRODUCT DETAILS ---");
        System.out.println("ID: " + product.getId());
        System.out.println("Name: " + product.getName());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Price: " + product.getPrice() + " kr");
        System.out.println("Stock quantity: " + product.getQuantity());
        System.out.println("Reorder level: " + product.getReorderLevel());
        System.out.println("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "None"));
        System.out.println("Number of suppliers: " + product.getSuppliers().size());
    }

    private static void displayCategoryDetails(Category category) {
        System.out.println("\n--- CATEGORY DETAILS ---");
        System.out.println("ID: " + category.getId());
        System.out.println("Name: " + category.getName());
        System.out.println("Description: " + category.getDescription());
        System.out.println("Product count: " + category.getProducts().size());
    }

    private static void displaySupplierDetails(Supplier supplier) {
        System.out.println("\n--- SUPPLIER DETAILS ---");
        System.out.println("ID: " + supplier.getId());
        System.out.println("Company: " + supplier.getName());
        System.out.println("Contact: " + supplier.getContactName());
        System.out.println("Email: " + supplier.getEmail());
        System.out.println("Phone: " + supplier.getPhone());
        System.out.println("Address: " + supplier.getAddress());
        System.out.println("Product count: " + supplier.getProducts().size());
    }

    // MOCK DATA INITIALIZATION
    private static void initializeMockData() {
        System.out.println("Initializing test data...");

        try {
            // Create Categories
            Category electronics = new Category("Electronics", "Electronic products and accessories");
            Category furniture = new Category("Furniture", "Office furniture and decor");
            Category stationery = new Category("Office Supplies", "Paper, pens and office materials");

            categoryService.saveCategory(electronics);
            categoryService.saveCategory(furniture);
            categoryService.saveCategory(stationery);

            // Create Suppliers
            Supplier techSupplier = new Supplier("Tech Solutions AB", "Anna Andersson", "anna@techsolutions.se", "08-123456", "Tech Street 1, Stockholm");
            Supplier furnitureSupplier = new Supplier("Furniture & More", "Erik Eriksson", "erik@furniture.se", "031-789012", "Furniture Road 5, Gothenburg");
            Supplier officeSupplier = new Supplier("Office Wholesaler", "Maria Nilsson", "maria@office.se", "040-345678", "Office Street 10, Malmö");

            supplierService.saveSupplier(techSupplier);
            supplierService.saveSupplier(furnitureSupplier);
            supplierService.saveSupplier(officeSupplier);

            // Create Products
            Product laptop = new Product("Laptop Dell XPS 13", "13-inch laptop with Intel i7", 15999.00, 25);
            laptop.setCategory(electronics);
            laptop.setReorderLevel(5);
            productService.saveProduct(laptop);

            Product mouse = new Product("Wireless mouse Logitech", "Ergonomic wireless mouse with USB receiver", 299.00, 50);
            mouse.setCategory(electronics);
            mouse.setReorderLevel(10);
            productService.saveProduct(mouse);

            Product keyboard = new Product("Mechanical keyboard", "RGB illuminated mechanical keyboard", 899.00, 30);
            keyboard.setCategory(electronics);
            keyboard.setReorderLevel(8);
            productService.saveProduct(keyboard);

            Product monitor = new Product("24-inch monitor", "Full HD IPS monitor with height-adjustable stand", 2499.00, 15);
            monitor.setCategory(electronics);
            monitor.setReorderLevel(3);
            productService.saveProduct(monitor);

            Product deskChair = new Product("Premium office chair", "Ergonomic office chair with armrests and height adjustment", 3999.00, 12);
            deskChair.setCategory(furniture);
            deskChair.setReorderLevel(2);
            productService.saveProduct(deskChair);

            Product desk = new Product("Desk 120x60", "White desk with metal legs", 1299.00, 8);
            desk.setCategory(furniture);
            desk.setReorderLevel(2);
            productService.saveProduct(desk);

            Product bookshelf = new Product("Bookshelf 5 shelves", "White melamine bookshelf with 5 adjustable shelves", 799.00, 6);
            bookshelf.setCategory(furniture);
            bookshelf.setReorderLevel(1);
            productService.saveProduct(bookshelf);

            Product fileCabinet = new Product("File cabinet 4 drawers", "Lockable gray metal file cabinet", 2199.00, 4);
            fileCabinet.setCategory(furniture);
            fileCabinet.setReorderLevel(1);
            productService.saveProduct(fileCabinet);

            Product paperA4 = new Product("A4 copy paper", "White copy paper 80g/m², 500 sheets/pack", 89.00, 200);
            paperA4.setCategory(stationery);
            paperA4.setReorderLevel(50);
            productService.saveProduct(paperA4);

            Product penBlue = new Product("Blue ballpoint pen", "Ballpoint pen with blue ink, pack of 10", 29.00, 150);
            penBlue.setCategory(stationery);
            penBlue.setReorderLevel(30);
            productService.saveProduct(penBlue);

            Product penRed = new Product("Red ballpoint pen", "Ballpoint pen with red ink, pack of 10", 29.00, 120);
            penRed.setCategory(stationery);
            penRed.setReorderLevel(30);
            productService.saveProduct(penRed);

            Product stapler = new Product("Stapler", "Heavy-duty stapler for up to 25 sheets", 149.00, 25);
            stapler.setCategory(stationery);
            stapler.setReorderLevel(5);
            productService.saveProduct(stapler);

            Product folders = new Product("A4 plastic folders", "Plastic folders with pocket, pack of 25", 199.00, 40);
            folders.setCategory(stationery);
            folders.setReorderLevel(10);
            productService.saveProduct(folders);

            Product calculator = new Product("Calculator", "Basic calculator with large display", 79.00, 35);
            calculator.setCategory(stationery);
            calculator.setReorderLevel(8);
            productService.saveProduct(calculator);

            Product whiteBoard = new Product("Whiteboard 90x60", "Magnetic whiteboard with aluminum frame", 599.00, 10);
            whiteBoard.setCategory(stationery);
            whiteBoard.setReorderLevel(2);
            productService.saveProduct(whiteBoard);

            // Create some sample transactions
            InventoryTransaction saleTransaction1 = new InventoryTransaction(-5, TransactionType.SALE, laptop);
            transactionService.saveTransaction(saleTransaction1);
            laptop.setQuantity(laptop.getQuantity() - 5);
            productService.updateProduct(laptop);

            InventoryTransaction restockTransaction1 = new InventoryTransaction(100, TransactionType.RESTOCK, paperA4);
            transactionService.saveTransaction(restockTransaction1);
            paperA4.setQuantity(paperA4.getQuantity() + 100);
            productService.updateProduct(paperA4);

            InventoryTransaction saleTransaction2 = new InventoryTransaction(-10, TransactionType.SALE, mouse);
            transactionService.saveTransaction(saleTransaction2);
            mouse.setQuantity(mouse.getQuantity() - 10);
            productService.updateProduct(mouse);

            InventoryTransaction saleTransaction3 = new InventoryTransaction(-2, TransactionType.SALE, deskChair);
            transactionService.saveTransaction(saleTransaction3);
            deskChair.setQuantity(deskChair.getQuantity() - 2);
            productService.updateProduct(deskChair);

            InventoryTransaction restockTransaction2 = new InventoryTransaction(50, TransactionType.RESTOCK, penBlue);
            transactionService.saveTransaction(restockTransaction2);
            penBlue.setQuantity(penBlue.getQuantity() + 50);
            productService.updateProduct(penBlue);

            System.out.println("Test data created successfully!");
            System.out.println("- " + categoryService.getAllCategories().size() + " categories");
            System.out.println("- " + supplierService.getAllSuppliers().size() + " suppliers");
            System.out.println("- " + productService.getAllProducts().size() + " products");
            System.out.println("- " + transactionService.getAllTransactions().size() + " transactions");

        } catch (Exception e) {
            System.out.println("Error creating test data: " + e.getMessage());
            // If mock data creation fails, continue anyway
        }
    }

    // INPUT UTILITY METHODS
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static long getLongInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                long value = Long.parseLong(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
}