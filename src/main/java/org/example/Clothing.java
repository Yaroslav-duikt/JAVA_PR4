package org.example;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Clothing extends Product {

    private final String size;
    private final String color;
    private final String material;

    @Builder
    public Clothing(String name, double price,
                    String size, String color, String material) {
        super(name, price);
        this.size = size;
        this.color = color;
        this.material = material;
    }

    @Override
    public String getCategory() {
        return "Одяг";
    }

    @Override
    public String toString() {
        return super.toString()
                + String.format(" | Розмір: %s | Колір: %s | Матеріал: %s",
                size, color, material);
    }
}
