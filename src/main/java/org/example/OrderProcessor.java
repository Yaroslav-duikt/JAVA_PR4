package org.example;

import java.util.List;
import java.util.function.Consumer;

public class OrderProcessor<T extends Product> {

    private final String processorName;

    public OrderProcessor(String processorName) {
        this.processorName = processorName;
    }

    public void process(Order<T> order, Consumer<Order<T>> handler)
            throws OrderProcessingException {

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new OrderProcessingException(
                    "Замовлення #" + order.getOrderId() + " вже виконано");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderProcessingException(
                    "Замовлення #" + order.getOrderId() + " скасовано і не може бути оброблено");
        }

        order.setStatus(OrderStatus.PROCESSING);
        handler.accept(order);         // виклик лямбди або посилання на метод
        order.setStatus(OrderStatus.COMPLETED);
    }

    public void processAll(List<Order<T>> orders, Consumer<Order<T>> handler) {
        // lambda: o -> process(o, handler)
        orders.forEach(order -> {
            try {
                process(order, handler);
            } catch (OrderProcessingException e) {
                System.err.printf("[%s] Помилка обробки: %s%n", processorName, e.getMessage());
            }
        });
    }

    public String getProcessorName() {
        return processorName;
    }

    public static void printOrder(Order<? extends Product> order) {
        System.out.printf("  Оброблено: %s%n", order);
    }
}
