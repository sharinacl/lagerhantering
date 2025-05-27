package se.yrgo.apputility;

import se.yrgo.entity.Category;
import se.yrgo.entity.Product;
import se.yrgo.entity.Supplier;

public class DisplayUtil {

    public static void displayProduct(Product product) {
        System.out.println("\n--- PRODUCT DETAILS ---");
        System.out.println("ID: " + product.getId());
        System.out.println("Name: " + product.getName());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Price: " + product.getPrice() + " kr");
        System.out.println("Stock quantity: " + product.getQuantity());
        System.out.println("Reorder level: " + product.getReorderLevel());
        System.out.println("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "None"));
        System.out.println("Number of suppliers: " + product.getSuppliers().size());
        System.out.println();
    }

    public static void displayCategory(Category category) {
        System.out.println("\n--- CATEGORY DETAILS ---");
        System.out.println("ID: " + category.getId());
        System.out.println("Name: " + category.getName());
        System.out.println("Description: " + category.getDescription());
        System.out.println("Product count: " + category.getProducts().size());
        System.out.println();
    }

    public static void displaySupplier(Supplier supplier) {
        System.out.println("\n--- SUPPLIER DETAILS ---");
        System.out.println("ID: " + supplier.getId());
        System.out.println("Company: " + supplier.getName());
        System.out.println("Contact: " + supplier.getContactName());
        System.out.println("Email: " + supplier.getEmail());
        System.out.println("Phone: " + supplier.getPhone());
        System.out.println("Address: " + supplier.getAddress());
        System.out.println("Product count: " + supplier.getProducts().size());
        System.out.println();
    }
}
