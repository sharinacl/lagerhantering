package se.yrgo.app;

import se.yrgo.entity.Category;
import se.yrgo.service.CategoryService;
import se.yrgo.apputility.DisplayUtil;

import java.util.List;
import java.util.Scanner;

public class CategoryMenuHandler {
    private final CategoryService categoryService;
    private final Scanner scanner;

    public CategoryMenuHandler(CategoryService categoryService, Scanner scanner) {
        this.categoryService = categoryService;
        this.scanner = scanner;
    }

    public void showMenu() {
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
                    case "1" -> handleCreateCategory();
                    case "2" -> handleListAll();
                    case "3" -> handleFindById();
                    case "4" -> handleFindByName();
                    case "5" -> handleUpdateCategory();
                    case "6" -> handleDeleteCategory();
                    case "7" -> handleDeleteAllCategories();
                    case "8" -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please select 1-8.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void handleCreateCategory() {
        try {
            System.out.print("Enter category name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Category name cannot be empty.");
                return;
            }
            System.out.print("Enter category description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Category description cannot be empty.");
            }
            Category category = new Category(name, description);
            categoryService.saveCategory(category);
            System.out.println("Category created successfully: ");
            DisplayUtil.displayCategory(category);
        } catch (Exception e) {
            System.err.println("Error creating category: " + e.getMessage());
        }
    }

    private void handleListAll() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
                return;
            }
            categories.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
        }
    }

    private void handleFindById() {
        try {
            System.out.print("Enter category ID: ");
            long id = Long.parseLong(scanner.nextLine());
            Category category = categoryService.getCategoryById(id);
            if (category != null) {
                DisplayUtil.displayCategory(category);
            } else {
                System.out.println("Category not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error finding category: " + e.getMessage());
        }
    }

    private void handleFindByName() {
        try {
            System.out.print("Enter category name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Category name cannot be empty.");
                return;
            }
            Category category = categoryService.getCategoryByName(name);
            if (category != null) {
                DisplayUtil.displayCategory(category);
            } else {
                System.out.println("Category not found with name: " + name);
            }
        } catch (Exception e) {
            System.err.println("Error finding category: " + e.getMessage());
        }
    }

    private void handleUpdateCategory() {
        try {
            System.out.print("Enter category ID you want to update: ");
            long id = Long.parseLong(scanner.nextLine());
            Category category = categoryService.getCategoryById(id);

            if (category == null) {
                System.out.println("Category not found with ID: " + id);
                return;
            }

            System.out.println("Current category information:");
            DisplayUtil.displayCategory(category);

            // Prompt for new name (optional)
            System.out.print("Enter new name (press Enter to keep '" + category.getName() + "'): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                category.setName(newName);
            }

            // Prompt for new description (optional)
            System.out.print("Enter new description (press Enter to keep '" + category.getDescription() + "'): ");
            String newDescription = scanner.nextLine().trim();
            if (!newDescription.isEmpty()) {
                category.setDescription(newDescription);
            }

            categoryService.updateCategory(category);
            System.out.println("Category updated successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error updating category: " + e.getMessage());
        }
    }

    private void handleDeleteCategory() {
        try {
            System.out.print("Enter category ID to delete: ");
            long id = Long.parseLong(scanner.nextLine());
            Category category = categoryService.getCategoryById(id);
            DisplayUtil.displayCategory(category);
            System.out.print("Are you sure you want to delete this category? (y/n): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y") || confirmation.equals("yes")) {
                categoryService.deleteCategory(id);
                System.out.println("Category deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error deleting category: " + e.getMessage());
        }
    }

    private void handleDeleteAllCategories() {
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
}
