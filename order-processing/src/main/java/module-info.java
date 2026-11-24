module order.processing {
    requires transitive order.model;
    requires order.storage;

    exports org.example.processing;
}
