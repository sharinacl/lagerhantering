package se.yrgo.entity;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToMany
    @JoinTable(
            name = "product_supplier",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    private Set<Supplier> suppliers = new HashSet<>();


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryTransaction> transactions = new ArrayList<>();

    // Default constructor required by JPA
    public Product() {
    }

    public Product(String name, String description, double price, Integer inventoryQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inventoryQuantity = inventoryQuantity;
    }

    public Product(String name, int quantity, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(String name) {
        this.name = name;
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


    public void addCategory(Category category) {
        this.category.add(category);
        category.getProducts().add(this);
    }

    public Set<Category> getCategories() {
        Set<Category> categories = Set.of();
        return categories;
    }

    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public List<InventoryTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<InventoryTransaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(InventoryTransaction transaction) {
        transactions.add(transaction);
        transaction.setProduct(this);
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + inventoryQuantity + "]";
    }
}



