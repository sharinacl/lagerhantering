package se.yrgo.entity;

public enum TransactionType {
    SALE("Sale to customer"),
    RESTOCK("Inventory restock"),
    RETURN("Product return"),
    ADJUSTMENT("Inventory adjustment"),
    DAMAGED("Damaged goods"),
    TRANSFER("Stock transfer");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
