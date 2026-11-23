package org.example;

import java.util.UUID;

public class Order<T extends Product> {

    private final String orderId;
    private final T product;
    private final int quantity;
    private OrderStatus status;

    public Order(T product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Товар у замовленні не може бути null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість товару повинна бути більше 0");
        }
        this.orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.product = product;
        this.quantity = quantity;
        this.status = OrderStatus.PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public T getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("Замовлення #%s | %s | К-ть: %d | Сума: %.2f грн | Статус: %s",
                orderId, product, quantity, getTotalPrice(), status);
    }
}
