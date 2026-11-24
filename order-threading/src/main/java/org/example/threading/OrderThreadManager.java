package org.example.threading;

import org.example.model.Order;
import org.example.model.Product;
import org.example.processing.OrderProcessor;
import org.example.storage.OrderRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Менеджер потоків для багатопотокової обробки замовлень.
 */
public class OrderThreadManager {

    private static final int THREAD_COUNT = 3;

    private final ExecutorService executor;
    private final OrderRepository repository;

    public OrderThreadManager(OrderRepository repository) {
        this.repository = repository;
        this.executor = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public <T extends Product> void submitOrders(
            List<Order<T>> orders,
            OrderProcessor<T> processor) {

        for (Order<T> order : orders) {
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.printf("[%s] Починає обробку: #%s%n",
                        threadName, order.getOrderId());
                try {
                    processor.process(order, OrderProcessor::printOrder);
                    repository.addOrder(order);
                } catch (Exception e) {
                    System.err.printf("[%s] Помилка: %s%n", threadName, e.getMessage());
                }
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Пул потоків не завершився вчасно, примусове зупинення...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Усі потоки завершено.");
    }
}
