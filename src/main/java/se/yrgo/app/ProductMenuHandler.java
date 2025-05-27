package se.yrgo.app;

import se.yrgo.apputility.DisplayUtil;
import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;
import se.yrgo.service.CategoryService;
import se.yrgo.service.ProductService;
import se.yrgo.service.SupplierService;

import java.util.List;
import java.util.Scanner;

public class ProductMenuHandler {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final Scanner scanner;

    public ProductMenuHandler(ProductService productService,
                              CategoryService categoryService,
                              SupplierService supplierService,
                              Scanner scanner) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.scanner = scanner;
    }

    public void showMenu() {
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
                    case "1" -> handleCreateProduct();
                    case "2" -> handleListAll();
                    case "3" -> handleFindById();
                    case "4" -> handleFindByName();
                    case "5" -> handleUpdateProduct();
                    case "6" -> handleDeleteProduct();
                    case "7" -> handleListByCategory();
                    case "8" -> handleListBySupplier();
                    case "9" -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please select 1-9.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void handleCreateProduct() {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Product name cannot be empty.");
                return;
            }

            System.out.print("Enter description: ");
            String desc = scanner.nextLine().trim();

            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine());
            if (price < 0) {
                System.out.println("Price cannot be negative.");
                return;
            }

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            if (quantity < 0) {
                System.out.println("Quantity cannot be negative.");
                return;
            }

            System.out.print("Enter reorder level: ");
            int reorderLevel = Integer.parseInt(scanner.nextLine());
            if (reorderLevel < 0) {
                System.out.println("Reorder level cannot be negative.");
                return;
            }

            var categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories available. Please create a category first.");
                return;
            }
            System.out.println("Available Categories:");
            categories.forEach(c -> System.out.printf("  %d: %s%n", c.getId(), c.getName()));
            System.out.print("Enter Category ID: ");
            Long catId = Long.parseLong(scanner.nextLine());
            Category category = categoryService.getCategoryById(catId);
            if (category == null) {
                System.out.println("Category not found with ID: " + catId);
                return;
            }

            var suppliers = supplierService.getAllSuppliers();
            if (suppliers.isEmpty()) {
                System.out.println("No suppliers available. Please create a supplier first.");
                return;
            }
            System.out.println("Available Suppliers:");
            suppliers.forEach(s -> System.out.printf("  %d: %s%n", s.getId(), s.getName()));
            System.out.print("Enter Supplier ID: ");
            Long supId = Long.parseLong(scanner.nextLine());
            Supplier supplier = supplierService.getSupplierById(supId);
            if (supplier == null) {
                System.out.println("Supplier not found with ID: " + supId);
                return;
            }

            Product p = new Product(name, desc, price, quantity, reorderLevel, category, supplier);
            productService.saveProduct(p);
            System.out.println("Product created successfully: " + p);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter valid numbers.");
        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
        }
    }

    private void handleListAll() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                System.out.println("No products found.");
                return;
            }
            products.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error retrieving products: " + e.getMessage());
        }
    }

    private void handleFindById() {
        try {
            System.out.print("Product ID: ");
            long id = Long.parseLong(scanner.nextLine());
            Product product = productService.getProductById(id);
            if (product != null) {
                DisplayUtil.displayProduct(product);
            } else {
                System.out.println("Product not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error finding product: " + e.getMessage());
        }
    }

    private void handleFindByName() {
        try {
            System.out.print("Product name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Product name cannot be empty.");
                return;
            }
            Product product = productService.getProductByName(name);
            if (product != null) {
                DisplayUtil.displayProduct(product);
            } else {
                System.out.println("Product not found with name: " + name);
            }
        } catch (Exception e) {
            System.err.println("Error finding product: " + e.getMessage());
        }
    }

    private void handleUpdateProduct() {
        try {
            System.out.print("Product ID: ");
            long pid = Long.parseLong(scanner.nextLine());
            Product p = productService.getProductById(pid);
            if (p == null) {
                System.out.println("Product not found with ID: " + pid);
                return;
            }

            System.out.println("Current product information:");
            DisplayUtil.displayProduct(p);

            System.out.print("Enter new name (press Enter to keep '" + p.getName() + "'): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                p.setName(newName);
            }

            System.out.print("Enter new description (press Enter to keep '" + p.getDescription() + "'): ");
            String newDesc = scanner.nextLine().trim();
            if (!newDesc.isEmpty()) {
                p.setDescription(newDesc);
            }

            System.out.print("Enter new price (press Enter to keep '" + p.getPrice() + "'): ");
            String priceInput = scanner.nextLine().trim();
            if (!priceInput.isEmpty()) {
                double newPrice = Double.parseDouble(priceInput);
                if (newPrice < 0) {
                    System.out.println("Price cannot be negative.");
                    return;
                }
                p.setPrice(newPrice);
            }

            System.out.print("Enter new quantity (press Enter to keep '" + p.getQuantity() + "'): ");
            String qtyInput = scanner.nextLine().trim();
            if (!qtyInput.isEmpty()) {
                int newQuantity = Integer.parseInt(qtyInput);
                if (newQuantity < 0) {
                    System.out.println("Quantity cannot be negative.");
                    return;
                }
                p.setQuantity(newQuantity);
            }

            // ✅ Update Category
            System.out.print("Enter new category ID (press Enter to keep current): ");
            String catInput = scanner.nextLine().trim();
            if (!catInput.isEmpty()) {
                long newCatId = Long.parseLong(catInput);
                Category newCategory = categoryService.getCategoryById(newCatId);
                if (newCategory != null) {
                    p.setCategory(newCategory);
                } else {
                    System.out.println("No category found with ID: " + newCatId);
                }
            }

            // ✅ Update Suppliers
            System.out.print("Do you want to update the suppliers? (y/n): ");
            String supplierDecision = scanner.nextLine().trim().toLowerCase();
            if (supplierDecision.equals("y") || supplierDecision.equals("yes")) {
                p.getSuppliers().clear();
                while (true) {
                    System.out.print("Enter supplier ID to add (or press Enter to finish): ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;

                    try {
                        long sid = Long.parseLong(input);
                        Supplier supplier = supplierService.getSupplierById(sid);
                        if (supplier != null) {
                            p.getSuppliers().add(supplier);
                            supplier.getProducts().add(p); // maintain bidirectional relationship
                            System.out.println("Added supplier: " + supplier.getName());
                        } else {
                            System.out.println("Supplier not found with ID: " + sid);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid supplier ID format.");
                    }
                }
            } else if (supplierDecision.equals("n") || supplierDecision.equals("no")) {
                p.getSuppliers().clear();
                while (true) {
                    System.out.print("Press Enter to finish: ");
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) break;
                }

            }

            productService.updateProduct(p);
            System.out.println("Product updated successfully.");
            DisplayUtil.displayProduct(p);
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter valid numbers.");
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
    }


    private void handleDeleteProduct() {
        try {
            System.out.print("Product ID to delete: ");
            long id = Long.parseLong(scanner.nextLine());

            Product product = productService.getProductById(id);
            if (product == null) {
                System.out.println("Product not found.");
                return;
            }

            // Show details before deletion
            DisplayUtil.displayProduct(product);

            System.out.print("Are you sure you want to delete this product? (y/n): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("y") && !confirmation.equals("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            // Safely detach associations
            Category category = product.getCategory();
            if (category != null && category.getProducts() != null) {
                category.getProducts().remove(product);
                product.setCategory(null);
            }

            if (product.getSuppliers() != null) {
                for (Supplier supplier : product.getSuppliers()) {
                    supplier.getProducts().remove(product);
                }
                product.getSuppliers().clear();
            }

            productService.deleteProduct(id);
            System.out.println("Product deleted successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }



    private void handleListByCategory() {
        try {
            System.out.print("Category ID: ");
            long catId = Long.parseLong(scanner.nextLine());
            Category cat = categoryService.getCategoryById(catId);
            if (cat == null) {
                System.out.println("Category not found with ID: " + catId);
                return;
            }
            List<Product> products = productService.getProductsByCategory(cat);
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

    private void handleListBySupplier() {
        try {
            System.out.print("Supplier ID: ");
            long supId = Long.parseLong(scanner.nextLine());
            Supplier s = supplierService.getSupplierById(supId);
            if (s == null) {
                System.out.println("Supplier not found with ID: " + supId);
                return;
            }
            List<Product> products = productService.getProductsBySupplier(s);
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
}
