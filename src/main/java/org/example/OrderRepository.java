package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository {

    // Синхронізований список для безпечної роботи з кількох потоків
    private final List<Order<? extends Product>> orders =
            Collections.synchronizedList(new ArrayList<>());

    public <T extends Product> void addOrder(Order<T> order) {
        orders.add(order);
    }

    public List<Order<? extends Product>> getAllOrders() {
        synchronized (orders) {
            return Collections.unmodifiableList(new ArrayList<>(orders));
        }
    }

    public List<Order<? extends Product>> getOrdersByStatus(OrderStatus status) {
        synchronized (orders) {
            return orders.stream()
                    .filter(o -> o.getStatus() == status)   // lambda
                    .collect(Collectors.toList());
        }
    }

    public List<Order<? extends Product>> getOrdersByCategory(String category) {
        synchronized (orders) {
            return orders.stream()
                    .filter(o -> o.getProduct().getCategory().equalsIgnoreCase(category)) // lambda
                    .collect(Collectors.toList());
        }
    }

    public int getTotalCount() {
        return orders.size();
    }
}
