package com.hcl.functional.model;

import java.util.Objects;

/**
 * Domain entity representing a Product for Functional Programming engine.
 */
public class Product {

    private final String id;
    private final String name;
    private final String category;
    private final double price;
    private final boolean inStock;
    private final double rating; // 1.0 to 5.0

    public Product(String id, String name, String category, double price, boolean inStock, double rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.inStock = inStock;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isInStock() {
        return inStock;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-26s | %-12s | $%,7.2f | InStock: %-5b | Rating: %.1f★",
                id, name, category, price, inStock, rating);
    }
}
