package me.bruhdows.skyblock.gui.api;

import lombok.Getter;

import java.util.function.Consumer;

public class InventoryListener<T> {

    @Getter private final Class<T> type;
    private final Consumer<T> consumer;

    public InventoryListener(Class<T> type, Consumer<T> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    public void accept(T t) { consumer.accept(t); }
}
