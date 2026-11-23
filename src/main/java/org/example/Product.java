package org.example;

public abstract class Product {

    private final String name;
    private final double price;

    public Product(String name, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва товару не може бути порожньою");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Ціна товару не може бути від'ємною");
        }
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    // Кожен підтип описує свою категорію
    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f грн", getCategory(), name, price);
    }
}
