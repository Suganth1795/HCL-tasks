package com.hcl.order.model;

/**
 * Product inventory entity.
 */
public class Product {

    private final String id;
    private final String name;
    private final double price;
    private int stockQuantity;

    public Product(String id, String name, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public synchronized void reduceStock(int quantity) {
        this.stockQuantity -= quantity;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-20s | Price: $%,.2f | In Stock: %d units",
                id, name, price, stockQuantity);
    }
}
