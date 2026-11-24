package org.example.app;

import com.github.javafaker.Faker;
import org.example.model.Clothing;
import org.example.model.Electronics;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Product;
import org.example.processing.OrderProcessor;
import org.example.storage.OrderRepository;
import org.example.threading.OrderThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Головний модуль додатка (order.app).
 * Демонструє взаємодію модулів через JPMS (module-info.java):
 *   - order.model: сутності Product, Electronics, Clothing, Order
 *   - order.storage: сховище OrderRepository (з прихованим пакетом internal)
 *   - order.processing: процесор OrderProcessor з Consumer<T>
 *   - order.threading: менеджер пулу потоків OrderThreadManager
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        Faker faker = new Faker(new Locale("en"));

        System.out.println("=".repeat(60));
        System.out.println("  МОДУЛЬНА БАГАТОПОТОКОВА СИСТЕМА ОБРОБКИ ЗАМОВЛЕНЬ");
        System.out.println("=".repeat(60));

        // 1. Генерація замовлень (Lombok @Builder + JavaFaker)
        List<Order<Electronics>> electronicsOrders = generateElectronicsOrders(faker, 5);
        List<Order<Clothing>>    clothingOrders    = generateClothingOrders(faker, 5);

        System.out.printf("%nСтворено %d замовлень на електроніку та %d замовлень на одяг.%n%n",
                electronicsOrders.size(), clothingOrders.size());

        // 2. Ініціалізація компонентів із підключених модулів
        OrderRepository repository = new OrderRepository();
        OrderThreadManager threadManager = new OrderThreadManager(repository);

        OrderProcessor<Electronics> electronicsProcessor = new OrderProcessor<>("Процесор-Електроніка");
        OrderProcessor<Clothing>    clothingProcessor    = new OrderProcessor<>("Процесор-Одяг");

        // 3. Багатопотокова обробка замовлень
        System.out.println("--- Запуск обробки замовлень на ЕЛЕКТРОНІКУ ---");
        threadManager.submitOrders(electronicsOrders, electronicsProcessor);

        System.out.println("--- Запуск обробки замовлень на ОДЯГ ---");
        threadManager.submitOrders(clothingOrders, clothingProcessor);

        // 4. Очікування завершення потоків
        threadManager.shutdown();

        // 5. Виведення результатів (Stream API + Лямбди + Method Reference)
        printResults(repository);
    }

    private static List<Order<Electronics>> generateElectronicsOrders(Faker faker, int count) {
        List<Order<Electronics>> orders = new ArrayList<>();
        String[] brands  = {"Samsung", "Apple",    "Sony",     "LG",      "Xiaomi"};
        String[] models  = {"Galaxy S24", "iPhone 16", "Xperia 5", "OLED TV", "14 Pro"};

        for (int i = 0; i < count; i++) {
            Electronics item = Electronics.builder()
                    .name(faker.commerce().productName())
                    .price(parsePrice(faker.commerce().price(500, 30000)))
                    .brand(brands[i % brands.length])
                    .model(models[i % models.length])
                    .warrantyMonths(faker.number().numberBetween(12, 36))
                    .build();

            orders.add(new Order<>(item, faker.number().numberBetween(1, 5)));
        }
        return orders;
    }

    private static List<Order<Clothing>> generateClothingOrders(Faker faker, int count) {
        List<Order<Clothing>> orders = new ArrayList<>();
        String[] sizes     = {"XS", "S", "M", "L", "XL"};
        String[] materials = {"Бавовна", "Поліестер", "Льон", "Шовк", "Вовна"};

        for (int i = 0; i < count; i++) {
            Clothing item = Clothing.builder()
                    .name(faker.commerce().productName())
                    .price(parsePrice(faker.commerce().price(200, 5000)))
                    .size(sizes[i % sizes.length])
                    .color(faker.color().name())
                    .material(materials[i % materials.length])
                    .build();

            orders.add(new Order<>(item, faker.number().numberBetween(1, 10)));
        }
        return orders;
    }

    private static void printResults(OrderRepository repository) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ПІДСУМОК ОБРОБЛЕНИХ ЗАМОВЛЕНЬ");
        System.out.println("=".repeat(60));

        List<Order<? extends Product>> allOrders = repository.getAllOrders();
        System.out.printf("Всього оброблено замовлень: %d%n%n", allOrders.size());

        System.out.println("--- Замовлення на ЕЛЕКТРОНІКУ ---");
        repository.getOrdersByCategory("Електроніка")
                .forEach(o -> System.out.println("  " + o));

        System.out.println("\n--- Замовлення на ОДЯГ ---");
        repository.getOrdersByCategory("Одяг")
                .forEach(o -> System.out.println("  " + o));

        double totalRevenue = allOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
        System.out.printf("%nЗагальна сума замовлень: %.2f грн%n", totalRevenue);

        long completedCount = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .count();
        System.out.printf("Статус COMPLETED: %d із %d%n", completedCount, allOrders.size());
    }

    private static double parsePrice(String priceStr) {
        try {
            return Double.parseDouble(priceStr.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
