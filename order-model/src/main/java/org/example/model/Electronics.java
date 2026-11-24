package org.example.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Товар категорії "Електроніка" з анотацією Lombok @Builder.
 */
@Getter
public class Electronics extends Product {

    private final String brand;
    private final String model;
    private final int warrantyMonths;

    @Builder
    public Electronics(String name, double price,
                       String brand, String model, int warrantyMonths) {
        super(name, price);
        this.brand = brand;
        this.model = model;
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getCategory() {
        return "Електроніка";
    }

    @Override
    public String toString() {
        return super.toString()
                + String.format(" | Бренд: %s | Модель: %s | Гарантія: %d міс.",
                brand, model, warrantyMonths);
    }
}
