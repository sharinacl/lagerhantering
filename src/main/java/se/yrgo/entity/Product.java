package se.yrgo.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private double price;

    @Column(name = "quantity", nullable = false)
    private Integer inventoryQuantity = 0;

    private int reorderLevel;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "product_supplier",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    private Set<Supplier> suppliers = new HashSet<>();


    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<InventoryTransaction> transactions = new HashSet<>();

    // Default constructor required by JPA
    public Product() {
    }

    public Product(String name, String description, double price, Category inventoryQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = inventoryQuantity;
    }

    public Product(String name, int quantity, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, String desc, double price,
                   int qty, int reorder, Category category, Supplier suppliers) {
        this.name = name;
        this.description = desc;
        this.price = price;
        this.inventoryQuantity = qty;
        this.reorderLevel = reorder;
        setCategory(category);               // use setter to handle both sides
        this.suppliers.add(suppliers);

    }

    public void setCategory(Category category) {
        this.category = category;
        category.getProducts().add(this);     // inverse-side hookup
    }

    /**
     * Adds a supplier to this product and maintains bidirectional relationship
     * @param supplier The supplier to add
     */
    public void addSupplier(Supplier supplier) {
        if (supplier != null) {
            this.suppliers.add(supplier);
            supplier.getProducts().add(this);
        }
    }

    /**
     * Removes a supplier from this product and maintains bidirectional relationship
     * @param supplier The supplier to remove
     */
    public void removeSupplier(Supplier supplier) {
        if (supplier != null) {
            this.suppliers.remove(supplier);
            supplier.getProducts().remove(this);
        }
    }

    /**
     * Checks if this product is supplied by a specific supplier
     * @param supplier The supplier to check
     * @return true if the supplier supplies this product
     */
    public boolean isSuppliedBy(Supplier supplier) {
        return suppliers.contains(supplier);
    }

    /**
     * Gets the number of suppliers for this product
     * @return the count of suppliers
     */
    public int getSupplierCount() {
        return suppliers.size();
    }

    /**
     * Gets all supplier names as a formatted string
     * @return comma-separated list of supplier names
     */
    public String getSupplierNames() {
        return suppliers.stream()
                .map(Supplier::getName)
                .reduce((name1, name2) -> name1 + ", " + name2)
                .orElse("No suppliers");
    }

    /**
     * Finds a supplier by name
     * @param name The supplier name to search for
     * @return the supplier if found, null otherwise
     */
    public Supplier findSupplierByName(String name) {
        return suppliers.stream()
                .filter(supplier -> supplier.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getQuantity() {
        return inventoryQuantity;
    }

    public void setQuantity(Integer quantity) {
        this.inventoryQuantity = quantity;
    }


//    public void addCategory(Category category) {
//        this.category.add(category);
//        category.getProducts().add(this);
//    }

    public Set<Category> getCategories() {
        if (category != null) {
            return Set.of(category);
        }
        return new HashSet<>();
    }

    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public Set<InventoryTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<InventoryTransaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(InventoryTransaction transaction) {
        transactions.add(transaction);
        transaction.setProduct(this);
    }

    @Override
    public String toString() {
        return String.format(
                "ProductID: %d, \nName: %s, \nPrice: %.2f, \nQuantity: %d, \nCategory: %s, \nSuppliers: %s\n",
                id,
                name,
                price,
                inventoryQuantity,
                category != null ? category.getName() : "none",
                String.join(", ", suppliers.stream()
                        .map(Supplier::getName)
                        .toList())
        );
    }


}



