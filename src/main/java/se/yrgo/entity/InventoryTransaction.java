package se.yrgo.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transaction")  // Explicit table name
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @Enumerated(EnumType.STRING)  // Store enum as string in database
    @Column(nullable = false, length = 20)
    private TransactionType type;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)  // Added fetch type for optimization
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Constructors
    public InventoryTransaction(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
        this.timestamp = LocalDateTime.now();
    }

    public InventoryTransaction(int quantity, TransactionType type, Product product) {
        this(quantity, product);
        this.type = type;
        this.product = product;
    }

    public InventoryTransaction() {

    }

    public InventoryTransaction(Long id, int i, TransactionType transactionType) {
        this.id = id;
        this.quantity = i;
        this.type = transactionType;
    }

    // Business method to validate transaction
    public boolean isValid() {
        if (type == null || product == null) {
            return false;
        }

        // Validate quantity based on transaction type
        switch (type) {
            case RESTOCK:
                return quantity > 0;
            case SALE:
                return quantity < 0;
            default:
                return false;
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %d units of %s",
                timestamp,
                type,
                Math.abs(quantity),  // Show absolute value for readability
                product != null ? product.getName() : "Unknown");
    }


}