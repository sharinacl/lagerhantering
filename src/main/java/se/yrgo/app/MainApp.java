package se.yrgo.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.entity.*;
import se.yrgo.service.*;

import java.util.*;

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        CategoryService categoryService = context.getBean(CategoryService.class);
        ProductService productService = context.getBean(ProductService.class);
        SupplierService supplierService = context.getBean(SupplierService.class);
        InventoryTransactionService transactionService = context.getBean(InventoryTransactionService.class);

        while (true) {
            System.out.println("\n==== Inventory Management System ====");
            System.out.println("1. Manage Categories");
            System.out.println("2. Manage Products");
            System.out.println("3. Manage Suppliers");
            System.out.println("4. Manage Transactions");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: handleCategories(categoryService); break;
                case 2: handleProducts(productService); break;
                case 3: handleSuppliers(supplierService); break;
                case 4: handleTransactions(transactionService); break;
                case 0: System.exit(0);
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void handleCategories(CategoryService service) {
        System.out.println("\n--- Manage Categories ---");
        System.out.println("1. List all");
        System.out.println("2. Add new");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            List<Category> categories = service.getAllCategories();
            categories.forEach(System.out::println);
        } else if (choice == 2) {
            System.out.print("Enter category name: ");
            String name = scanner.nextLine();
            service.saveCategory(new Category(name));
            System.out.println("Category added.");
        }
    }

    private static void handleProducts(ProductService service) {
        System.out.println("\n--- Manage Products ---");
        System.out.println("1. List all");
        System.out.println("2. Add new");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            List<Product> products = service.getAllProducts();
            products.forEach(System.out::println);
        } else if (choice == 2) {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            service.saveProduct(new Product(name));
            System.out.println("Product added.");
        }
    }

    private static void handleSuppliers(SupplierService service) {
        System.out.println("\n--- Manage Suppliers ---");
        System.out.println("1. List all");
        System.out.println("2. Add new");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            List<Supplier> suppliers = service.getAllSuppliers();
            suppliers.forEach(System.out::println);
        } else if (choice == 2) {
            System.out.print("Enter supplier name: ");
            String name = scanner.nextLine();
            service.saveSupplier(new Supplier(name));
            System.out.println("Supplier added.");
        }
    }

    private static void handleTransactions(InventoryTransactionService service) {
        System.out.println("\n--- Manage Transactions ---");
        System.out.println("1. List all");
        System.out.println("2. Add new");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            List<InventoryTransaction> transactions = service.getAllTransactions();
            transactions.forEach(System.out::println);
        } else if (choice == 2) {
            System.out.print("Enter transaction type: ");
            String type = scanner.nextLine();
            service.saveTransaction(new InventoryTransaction(type));
            System.out.println("Transaction added.");
        }
    }
}
