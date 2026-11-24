package org.example.storage;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Product;
import org.example.storage.internal.StorageValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сховище замовлень — зберігає та надає доступ до замовлень.
 * Використовує синхронізований список для потокобезпечності.
 */
public class OrderRepository {

    private final List<Order<? extends Product>> orders =
            Collections.synchronizedList(new ArrayList<>());

    public <T extends Product> void addOrder(Order<T> order) {
        // Використання внутрішнього класу модуля (доступний лише всередині order.storage):
        if (StorageValidator.isValidOrder(order)) {
            orders.add(order);
        } else {
            throw new IllegalArgumentException("Замовлення не пройшло внутрішню валідацію");
        }
    }

    public List<Order<? extends Product>> getAllOrders() {
        synchronized (orders) {
            return Collections.unmodifiableList(new ArrayList<>(orders));
        }
    }

    public List<Order<? extends Product>> getOrdersByStatus(OrderStatus status) {
        synchronized (orders) {
            return orders.stream()
                    .filter(o -> o.getStatus() == status)
                    .collect(Collectors.toList());
        }
    }

    public List<Order<? extends Product>> getOrdersByCategory(String category) {
        synchronized (orders) {
            return orders.stream()
                    .filter(o -> o.getProduct().getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
    }

    public int getTotalCount() {
        return orders.size();
    }
}
