module order.storage {
    requires transitive order.model;

    // Публічне API сховища експортується:
    exports org.example.storage;

    // Внутрішній пакет org.example.storage.internal НЕ експортується!
    // Інші модулі не мають до нього доступу (Strong Encapsulation).
}
