package se.yrgo.app;

import se.yrgo.entity.Product;
import se.yrgo.service.InventoryTransactionService;
import se.yrgo.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class InventoryMenuHandler {
    private final InventoryTransactionService inventoryService;
    private final ProductService productService;
    private final Scanner scanner;

    public InventoryMenuHandler(InventoryTransactionService inventoryService,
                                ProductService productService,
                                Scanner scanner) {
        this.inventoryService = inventoryService;
        this.productService   = productService;
        this.scanner          = scanner;
    }

    public void showMenu() {
        while (true) {
            try {
                System.out.println("\n--- Inventory Transactions ---");
                System.out.println("1. Record Sale");
                System.out.println("2. Record Restock");
                System.out.println("3. Record Return");
                System.out.println("4. View Stock Level");
                System.out.println("5. List All Transactions");
                System.out.println("6. View All Stock Levels");
                System.out.println("7. Back");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> handleRecordSale();
                    case "2" -> handleRecordRestock();
                    case "3" -> handleRecordReturn();
                    case "4" -> handleViewStockLevel();
                    case "5" -> handleListAllTransactions();
                    case "6" -> handleViewAllStockLevels();
                    case "7" -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please select 1-7.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private void handleRecordSale() {
        try {
            System.out.print("Product ID: ");
            long pid = Long.parseLong(scanner.nextLine());
            System.out.print("Qty sold: ");
            int q = Integer.parseInt(scanner.nextLine());
            if (q <= 0) {
                System.out.println("Quantity sold must be positive.");
                return;
            }
            inventoryService.recordSale(pid, q);
            System.out.println("Sale recorded successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter valid numbers.");
        } catch (Exception e) {
            System.err.println("Error recording sale: " + e.getMessage());
        }
    }

    private void handleRecordRestock() {
        try {
            System.out.print("Product ID: ");
            long pid = Long.parseLong(scanner.nextLine());
            System.out.print("Qty restocked: ");
            int q = Integer.parseInt(scanner.nextLine());
            if (q <= 0) {
                System.out.println("Quantity restocked must be positive.");
                return;
            }
            inventoryService.recordRestock(pid, q);
            System.out.println("Restock recorded successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter valid numbers.");
        } catch (Exception e) {
            System.err.println("Error recording restock: " + e.getMessage());
        }
    }

    private void handleRecordReturn() {
        try {
            System.out.print("Product ID: ");
            long pid = Long.parseLong(scanner.nextLine());
            System.out.print("Qty returned: ");
            int q = Integer.parseInt(scanner.nextLine());
            if (q <= 0) {
                System.out.println("Quantity returned must be positive.");
                return;
            }
            inventoryService.recordReturn(pid, q);
            System.out.println("Return recorded successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter valid numbers.");
        } catch (Exception e) {
            System.err.println("Error recording return: " + e.getMessage());
        }
    }

    private void handleViewStockLevel() {
        try {
            System.out.print("Product ID: ");
            long pid = Long.parseLong(scanner.nextLine());
            int stock = inventoryService.getProductStockLevel(pid);
            System.out.println("Stock level: " + stock);
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error retrieving stock level: " + e.getMessage());
        }
    }

    private void handleListAllTransactions() {
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

    private void handleViewAllStockLevels() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                System.out.println("No products found.");
                return;
            }
            System.out.printf("%-5s  %-25s  %s%n", "ID", "Name", "Stock");
            System.out.println("------------------------------------------------");
            for (Product p : products) {
                System.out.printf("%-5d  %-25s  %d%n",
                        p.getId(), p.getName(), p.getQuantity());
            }
        } catch (Exception e) {
            System.err.println("Error retrieving stock levels: " + e.getMessage());
        }
    }
}
