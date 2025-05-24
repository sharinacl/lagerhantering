package se.yrgo.app;

import org.springframework.context.ApplicationContext;
import se.yrgo.entity.*;
import se.yrgo.service.*;

/**
 * Utility class for initializing mock data in the warehouse management system.
 * This can be run separately or integrated into the main application.
 */
public class MockDataInitializer {

    private ProductService productService;
    private SupplierService supplierService;
    private CategoryService categoryService;
    private InventoryTransactionService transactionService;

    public MockDataInitializer(ApplicationContext context) {
        this.productService = context.getBean(ProductService.class);
        this.supplierService = context.getBean(SupplierService.class);
        this.categoryService = context.getBean(CategoryService.class);
        this.transactionService = context.getBean(InventoryTransactionService.class);
    }

    public void initializeAllData() {
        System.out.println("=== INITIALIZING COMPLETE TEST DATA ===");

        createCategories();
        createSuppliers();
        createProducts();
        createTransactions();

        printSummary();
    }

    private void createCategories() {
        System.out.println("Creating categories...");

        Category[] categories = {
                new Category("Electronics", "Electronic products and accessories"),
                new Category("Furniture", "Office furniture and interior design"),
                new Category("Office Supplies", "Paper, pens and office materials"),
                new Category("Lighting", "Lamps and lighting products"),
                new Category("Storage Solutions", "Boxes, shelves and storage products"),
                new Category("Security", "Security products and locks"),
                new Category("Cleaning", "Cleaning materials and products"),
                new Category("Tech & IT", "Advanced technical products and IT equipment")
        };

        for (Category category : categories) {
            categoryService.saveCategory(category);
        }

        System.out.println("Created " + categories.length + " categories.");
    }

    private void createSuppliers() {
        System.out.println("Creating suppliers...");

        Supplier[] suppliers = {
                new Supplier("Tech Solutions AB", "Anna Anderson", "anna@techsolutions.se", "08-123456", "Tech Street 1, 111 20 Stockholm"),
                new Supplier("Furniture & More", "Erik Eriksson", "erik@furniture.se", "031-789012", "Furniture Way 5, 412 55 Gothenburg"),
                new Supplier("Office Wholesaler", "Maria Nilsson", "maria@office.se", "040-345678", "Office Road 10, 211 22 Malmö"),
                new Supplier("Nordic Light AB", "Lars Larsson", "lars@nordiclight.se", "019-456789", "Light Street 15, 721 30 Västerås"),
                new Supplier("Security Company", "Ingrid Svensson", "ingrid@security.se", "018-567890", "Security Street 3, 753 20 Uppsala"),
                new Supplier("CleanPro Sweden", "Johan Johansson", "johan@cleanpro.se", "054-678901", "Clean Street 7, 651 80 Karlstad"),
                new Supplier("DataTech Nordic", "Sofia Persson", "sofia@datatech.se", "013-789012", "Data Way 12, 582 20 Linköping"),
                new Supplier("Office Depot Scandinavia", "Michael Berg", "michael.berg@officedepot.se", "021-890123", "Office Park 8, 721 30 Västerås"),
                new Supplier("Scandinavian Furniture Co", "Astrid Holm", "astrid@scanfurn.se", "046-901234", "Design Way 4, 222 20 Lund"),
                new Supplier("ElektroMax AB", "Peter Gustafsson", "peter@elektromax.se", "090-012345", "Electronics Street 9, 903 30 Umeå")
        };

        for (Supplier supplier : suppliers) {
            supplierService.saveSupplier(supplier);
        }

        System.out.println("Created " + suppliers.length + " suppliers.");
    }

    private void createProducts() {
        System.out.println("Creating products...");

        // Get categories for assignment
        Category electronics = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Electronics")).findFirst().orElse(null);
        Category furniture = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Furniture")).findFirst().orElse(null);
        Category stationery = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Office Supplies")).findFirst().orElse(null);
        Category lighting = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Lighting")).findFirst().orElse(null);
        Category storage = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Storage Solutions")).findFirst().orElse(null);
        Category security = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Security")).findFirst().orElse(null);
        Category cleaning = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Cleaning")).findFirst().orElse(null);
        Category tech = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equals("Tech & IT")).findFirst().orElse(null);

        // Electronics Products
        createProduct("Dell XPS 13 Laptop", "13-inch laptop with Intel i7, 16GB RAM", 15999.00, 25, 5, electronics);
        createProduct("HP EliteBook Laptop", "14-inch business laptop with Intel i5", 12999.00, 18, 4, electronics);
        createProduct("Dell OptiPlex Desktop", "Desktop computer for office with Intel i5", 8999.00, 12, 3, electronics);
        createProduct("Logitech Wireless Mouse", "Ergonomic wireless mouse with USB receiver", 299.00, 50, 10, electronics);
        createProduct("Microsoft Wireless Mouse", "Precision mouse with BlueTrack technology", 399.00, 35, 8, electronics);
        createProduct("Mechanical Keyboard", "RGB backlit mechanical keyboard with Cherry MX", 899.00, 30, 8, electronics);
        createProduct("Wireless Keyboard", "Compact wireless keyboard with Nordic layout", 599.00, 25, 6, electronics);
        createProduct("24-inch Monitor", "Full HD IPS monitor with height-adjustable stand", 2499.00, 15, 3, electronics);
        createProduct("27-inch Monitor", "QHD IPS monitor with USB-C docking", 4999.00, 8, 2, electronics);
        createProduct("32-inch Monitor", "4K UHD monitor for professional use", 7999.00, 5, 1, electronics);
        createProduct("HD Webcam", "1080p webcam with auto focus", 799.00, 40, 10, electronics);
        createProduct("Headset with Microphone", "Professional headset for video conferences", 1299.00, 20, 5, electronics);
        createProduct("Bluetooth Speakers", "Wireless stereo speakers for office", 899.00, 15, 4, electronics);
        createProduct("HP LaserJet Printer", "Black and white laser printer with duplex", 3999.00, 6, 2, electronics);
        createProduct("Multifunction Printer", "Color multifunction printer with WiFi", 5999.00, 4, 1, electronics);

        // Furniture Products
        createProduct("Premium Office Chair", "Ergonomic office chair with armrests and height adjustment", 3999.00, 12, 2, furniture);
        createProduct("Basic Office Chair", "Simple office chair with wheels and height adjustment", 1999.00, 20, 4, furniture);
        createProduct("Executive Office Chair", "Leather office chair with massage function", 7999.00, 5, 1, furniture);
        createProduct("Desk 120x60", "White desk with metal legs", 1299.00, 8, 2, furniture);
        createProduct("Desk 140x70", "Larger oak desk with cable management", 2299.00, 6, 1, furniture);
        createProduct("Height Adjustable Desk", "Electric height adjustable desk", 4999.00, 4, 1, furniture);
        createProduct("Bookshelf 5 Shelves", "White melamine bookshelf with adjustable shelves", 799.00, 10, 2, furniture);
        createProduct("Filing Cabinet 4 Drawers", "Lockable filing cabinet in gray metal", 2199.00, 4, 1, furniture);
        createProduct("Filing Cabinet 2 Drawers", "Smaller filing cabinet for personal storage", 1499.00, 8, 2, furniture);
        createProduct("Storage Cabinet", "Tall storage cabinet with adjustable shelves", 1899.00, 6, 1, furniture);
        createProduct("3-Seat Sofa", "Comfortable sofa for break room in gray fabric", 5999.00, 3, 1, furniture);
        createProduct("Armchair", "Ergonomic armchair for reading and relaxation", 2999.00, 5, 1, furniture);
        createProduct("Coffee Table", "Modern coffee table in white high gloss", 1299.00, 4, 1, furniture);
        createProduct("Round Conference Table", "Round conference table for 6 people", 4999.00, 2, 1, furniture);
        createProduct("Rectangular Conference Table", "Large conference table for 12 people", 8999.00, 1, 1, furniture);

        // Office Supplies Products
        createProduct("A4 Copy Paper", "White copy paper 80g/m², 500 sheets/pack", 89.00, 200, 50, stationery);
        createProduct("A3 Copy Paper", "White copy paper 80g/m² A3 format", 159.00, 100, 25, stationery);
        createProduct("Blue Ballpoint Pen", "Ballpoint pen with blue ink, pack of 10", 29.00, 150, 30, stationery);
        createProduct("Red Ballpoint Pen", "Ballpoint pen with red ink, pack of 10", 29.00, 120, 30, stationery);
        createProduct("Black Ballpoint Pen", "Ballpoint pen with black ink, pack of 10", 29.00, 140, 30, stationery);
        createProduct("Highlighter Markers", "Colorful highlighter markers, set of 8", 99.00, 80, 20, stationery);
        createProduct("Whiteboard Markers", "Whiteboard markers in various colors, 4-pack", 149.00, 60, 15, stationery);
        createProduct("Stapler", "Robust stapler for up to 25 sheets", 149.00, 25, 5, stationery);
        createProduct("Staples", "Standard staples 26/6, box of 5000", 39.00, 100, 25, stationery);
        createProduct("A4 Plastic Folders", "Plastic folders with pocket, pack of 25", 199.00, 40, 10, stationery);
        createProduct("Paper Binders", "Paper binders with tabs, 10-pack", 149.00, 30, 8, stationery);
        createProduct("Calculator", "Basic calculator with large display", 79.00, 35, 8, stationery);
        createProduct("Whiteboard 90x60", "Magnetic whiteboard with aluminum frame", 599.00, 10, 2, stationery);
        createProduct("Whiteboard 120x90", "Large magnetic whiteboard for conference", 999.00, 5, 1, stationery);
        createProduct("Cork Boards", "Cork boards for bulletin boards, 60x45 cm", 199.00, 15, 4, stationery);

        // Lighting Products
        createProduct("LED Desk Lamp", "Adjustable LED lamp with USB charging", 599.00, 20, 5, lighting);
        createProduct("LED Ceiling Panel", "Energy efficient LED panel for office", 1299.00, 12, 3, lighting);
        createProduct("Office Floor Lamp", "Modern floor lamp with dimmer", 1999.00, 8, 2, lighting);
        createProduct("Halogen Desk Lamp", "Classic halogen lamp with adjustable arm", 399.00, 15, 4, lighting);
        createProduct("LED Strip", "Flexible LED strip for indirect lighting", 299.00, 25, 6, lighting);

        // Storage Solutions
        createProduct("Plastic Storage Boxes", "Stackable storage boxes, set of 5", 299.00, 30, 8, storage);
        createProduct("Document Boxes", "Document boxes in corrugated cardboard, 10-pack", 199.00, 40, 10, storage);
        createProduct("File Sorter", "Desktop file sorter in wood", 249.00, 20, 5, storage);
        createProduct("CD/DVD Storage", "Storage box for CD/DVD discs", 149.00, 25, 6, storage);
        createProduct("Letter Trays", "Stackable letter trays in metal, 3-pack", 199.00, 18, 4, storage);

        // Security Products
        createProduct("Paper Shredder", "Cross-cut paper shredder for office", 1999.00, 6, 2, security);
        createProduct("Safe", "Digital safe with dual locks", 3999.00, 3, 1, security);
        createProduct("Security Camera", "WiFi security camera with motion detector", 1299.00, 8, 2, security);
        createProduct("Lockable Cabinets", "Personal lockable storage cabinets", 899.00, 10, 2, security);
        createProduct("Security Locks", "High security locks for doors, 5-pack", 599.00, 15, 4, security);

        // Cleaning Products
        createProduct("Office Vacuum Cleaner", "Compact vacuum cleaner for office", 1999.00, 5, 1, cleaning);
        createProduct("Cleaning Solution", "Eco-friendly cleaning solution, 5-liter container", 199.00, 20, 5, cleaning);
        createProduct("Paper Towels", "Absorbent paper towels, 12-pack", 299.00, 50, 12, cleaning);
        createProduct("Toilet Paper", "Soft toilet paper, 24-pack", 399.00, 60, 15, cleaning);
        createProduct("Trash Bins", "Trash bins with lids, various sizes, 3-pack", 249.00, 15, 4, cleaning);

        // Tech & IT Products
        createProduct("Server Rack", "19-inch server rack for network equipment", 15999.00, 2, 1, tech);
        createProduct("Network Switch", "24-port Gigabit network switch", 2999.00, 4, 1, tech);
        createProduct("WiFi Router", "Professional WiFi 6 router for office", 1999.00, 8, 2, tech);
        createProduct("UPS Unit", "Uninterruptible power supply 1500VA", 3999.00, 6, 2, tech);
        createProduct("External Hard Drive", "2TB external hard drive with USB 3.0", 899.00, 20, 5, tech);
        createProduct("SSD Drive", "1TB SSD drive for fast data storage", 1299.00, 15, 4, tech);
        createProduct("RAM Memory", "16GB DDR4 RAM memory for upgrade", 799.00, 25, 6, tech);
        createProduct("Graphics Card", "Professional graphics card for design", 8999.00, 3, 1, tech);
        createProduct("KVM Switch", "4-port KVM switch for multiple computers", 1499.00, 8, 2, tech);
        createProduct("Projector Screen", "Electric projector screen 200x150 cm", 2999.00, 4, 1, tech);

        System.out.println("Created all products.");
    }

    private void createProduct(String name, String description, double price, int quantity, int reorderLevel, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setReorderLevel(reorderLevel);
        if (category != null) {
            product.setCategory(category);
        }
        productService.saveProduct(product);
    }

    private void createTransactions() {
        System.out.println("Creating transactions...");

        // Get some products for transactions
        java.util.List<Product> allProducts = productService.getAllProducts();

        if (!allProducts.isEmpty()) {
            // Create various transaction examples
            createSampleTransactions(allProducts);
        }

        System.out.println("Created all transactions.");
    }

    private void createSampleTransactions(java.util.List<Product> products) {
        // Sales transactions
        for (int i = 0; i < Math.min(10, products.size()); i++) {
            Product product = products.get(i);
            int saleQuantity = Math.min(5, product.getQuantity() / 2);
            if (saleQuantity > 0) {
                InventoryTransaction sale = new InventoryTransaction(-saleQuantity, TransactionType.SALE, product);
                transactionService.saveTransaction(sale);
                product.setQuantity(product.getQuantity() - saleQuantity);
                productService.updateProduct(product);
            }
        }

        // Restock transactions
        for (int i = 5; i < Math.min(15, products.size()); i++) {
            Product product = products.get(i);
            int restockQuantity = product.getReorderLevel() * 3;
            InventoryTransaction restock = new InventoryTransaction(restockQuantity, TransactionType.RESTOCK, product);
            transactionService.saveTransaction(restock);
            product.setQuantity(product.getQuantity() + restockQuantity);
            productService.updateProduct(product);
        }

        // More diverse transactions
        for (int i = 10; i < Math.min(25, products.size()); i++) {
            Product product = products.get(i);

            // Random sale
            if (Math.random() > 0.5 && product.getQuantity() > 5) {
                int saleQty = (int)(Math.random() * 5) + 1;
                InventoryTransaction sale = new InventoryTransaction(-saleQty, TransactionType.SALE, product);
                transactionService.saveTransaction(sale);
                product.setQuantity(product.getQuantity() - saleQty);
                productService.updateProduct(product);
            }

            // Random restock
            if (Math.random() > 0.7) {
                int restockQty = (int)(Math.random() * 20) + 10;
                InventoryTransaction restock = new InventoryTransaction(restockQty, TransactionType.RESTOCK, product);
                transactionService.saveTransaction(restock);
                product.setQuantity(product.getQuantity() + restockQty);
                productService.updateProduct(product);
            }
        }
    }

    private void printSummary() {
        System.out.println("\n=== TEST DATA SUMMARY ===");
        System.out.println("Categories: " + categoryService.getAllCategories().size());
        System.out.println("Suppliers: " + supplierService.getAllSuppliers().size());
        System.out.println("Products: " + productService.getAllProducts().size());
        System.out.println("Transactions: " + transactionService.getAllTransactions().size());
        System.out.println("=== DATA INITIALIZATION COMPLETE ===");
    }

    /**
     * Alternative method to initialize only basic data without transactions
     */
    public void initializeBasicData() {
        System.out.println("=== INITIALIZING BASIC TEST DATA ===");

        createCategories();
        createSuppliers();
        createProducts();

        System.out.println("\n=== BASIC DATA SUMMARY ===");
        System.out.println("Categories: " + categoryService.getAllCategories().size());
        System.out.println("Suppliers: " + supplierService.getAllSuppliers().size());
        System.out.println("Products: " + productService.getAllProducts().size());
        System.out.println("=== BASIC DATA INITIALIZATION COMPLETE ===");
    }

    /**
     * Method to clear all data (useful for testing)
     */
    public void clearAllData() {
        System.out.println("=== CLEARING ALL DATA ===");

        // Note: Order matters due to foreign key constraints
        transactionService.deleteAllTransactions();
        productService.deleteAllProducts();
        categoryService.deleteAllCategories();
        supplierService.deleteAllSuppliers();

        System.out.println("All data cleared successfully.");
    }
}