package se.yrgo.entity;

public enum TransactionType {
    SALE("Sale to customer"),
    RESTOCK("Inventory restock"),
    RETURN("Product return");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
