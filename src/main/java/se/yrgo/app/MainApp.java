package se.yrgo.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.service.CategoryService;
import se.yrgo.service.InventoryTransactionService;
import se.yrgo.service.ProductService;
import se.yrgo.service.SupplierService;

import java.util.Scanner;

public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // load Spring context
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (ctx != null) {
                ctx.close();
            }
        }));

        // lookup services
        CategoryService categoryService = ctx.getBean(CategoryService.class);
        ProductService productService = ctx.getBean(ProductService.class);
        SupplierService supplierService = ctx.getBean(SupplierService.class);
        InventoryTransactionService inventoryService = ctx.getBean(InventoryTransactionService.class);

        // initialize menu handlers
        CategoryMenuHandler categoryMenu = new CategoryMenuHandler(categoryService, scanner);
        ProductMenuHandler productMenu = new ProductMenuHandler(productService,
                categoryService,
                supplierService,
                scanner);
        SupplierMenuHandler supplierMenu = new SupplierMenuHandler(supplierService, scanner);
        InventoryMenuHandler inventoryMenu = new InventoryMenuHandler(inventoryService,
                productService,
                scanner);

        // main loop
        while (true) {
            System.out.println("\n=== Inventory Management System ===");
            System.out.println("1. Manage Categories");
            System.out.println("2. Manage Products");
            System.out.println("3. Manage Suppliers");
            System.out.println("4. Manage Inventory Transactions");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> categoryMenu.showMenu();
                case "2" -> productMenu.showMenu();
                case "3" -> supplierMenu.showMenu();
                case "4" -> inventoryMenu.showMenu();
                case "5" -> {
                    System.out.println("Exiting. Goodbye!");
                    ((ClassPathXmlApplicationContext) ctx).close();
                    return;
                }
                default -> System.out.println("Invalid option. Please select 1-5.");
            }
        }
    }
}
