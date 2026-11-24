module order.app {
    requires order.model;
    requires order.storage;
    requires order.processing;
    requires order.threading;
    requires javafaker;
    requires java.sql; // Потрібно для SnakeYaml (JavaFaker) у модульному режимі
}
