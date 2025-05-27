package se.yrgo.app;

import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import se.yrgo.service.SupplierService;

import java.util.List;
import java.util.Scanner;

public class SupplierMenuHandler {
    private final SupplierService supplierService;
    private final Scanner scanner;

    public SupplierMenuHandler(SupplierService supplierService, Scanner scanner) {
        this.supplierService = supplierService;
        this.scanner = scanner;
    }

    public void showMenu() {
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
                    case "1" -> handleCreateSupplier();
                    case "2" -> handleListAll();
                    case "3" -> handleFindById();
                    case "4" -> handleUpdateSupplier();
                    case "5" -> handleDeleteSupplier();
                    case "6" -> handleListProductsForSupplier();
                    case "7" -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please select 1-7.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void handleCreateSupplier() {
        try {
            System.out.print("Supplier name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Supplier name cannot be empty.");
                return;
            }
            System.out.print("Enter contact person: ");
            String contact = scanner.nextLine().trim();
            if (contact.isEmpty()) {
                System.out.println("Supplier contact person cannot be empty.");
                return;
            }
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Supplier email cannot be empty.");
                return;
            }
            System.out.print("Enter contact number (+46739-1234-56): ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                System.out.println("Supplier contact number cannot be empty.");
                return;
            }
            System.out.print("Enter address: ");
            String address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Supplier address cannot be empty.");
                return;
            }
            Supplier s = new Supplier(name, contact, email, phone, address);
            supplierService.saveSupplier(s);
            System.out.println("Supplier created successfully: " + s);
        } catch (Exception e) {
            System.err.println("Error creating supplier: " + e.getMessage());
        }
    }

    private void handleListAll() {
        try {
            List<Supplier> suppliers = supplierService.getAllSuppliers();
            if (suppliers.isEmpty()) {
                System.out.println("No suppliers found.");
                return;
            }
            suppliers.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error retrieving suppliers: " + e.getMessage());
        }
    }

    private void handleFindById() {
        try {
            System.out.print("Supplier ID: ");
            long id = Long.parseLong(scanner.nextLine());
            Supplier s = supplierService.getSupplierById(id);
            if (s != null) {
                System.out.println(s);
            } else {
                System.out.println("Supplier not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error finding supplier: " + e.getMessage());
        }
    }

    private void handleUpdateSupplier() {
        try {
            System.out.print("Supplier ID: ");
            long id = Long.parseLong(scanner.nextLine());
            Supplier s = supplierService.getSupplierById(id);
            if (s == null) {
                System.out.println("Supplier not found with ID: " + id);
                return;
            }
            System.out.print("New name (" + s.getName() + "): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) s.setName(newName);
            supplierService.updateSupplier(s);
            System.out.println("Supplier updated successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error updating supplier: " + e.getMessage());
        }
    }

    private void handleDeleteSupplier() {
        try {
            System.out.print("Supplier ID to delete: ");
            long id = Long.parseLong(scanner.nextLine());
            supplierService.deleteSupplierById(id);
            System.out.println("Supplier deleted successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
        }
    }

    private void handleListProductsForSupplier() {
        try {
            System.out.print("Supplier ID: ");
            long id = Long.parseLong(scanner.nextLine());
            List<Product> products = supplierService.getProductsForSupplier(id);
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
}