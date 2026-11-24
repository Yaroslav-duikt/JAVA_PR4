package org.example.storage.internal;

import org.example.model.Order;
import org.example.model.Product;

/**
 * Внутрішній допоміжний клас модуля order.storage.
 *
 * УВАГА: цей пакет НЕ експортовано в module-info.java!
 * Завдяки механізму захисту доступу модулів (JPMS Strong Encapsulation),
 * жоден інший модуль (order.processing, order.threading, order.app)
 * НЕ МОЖЕ імпортувати чи викликати цей клас.
 */
public class StorageValidator {

    public static <T extends Product> boolean isValidOrder(Order<T> order) {
        return order != null && order.getProduct() != null && order.getQuantity() > 0;
    }
}
