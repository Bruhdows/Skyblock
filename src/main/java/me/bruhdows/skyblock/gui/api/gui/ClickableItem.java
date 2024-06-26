package me.bruhdows.skyblock.gui.api.gui;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ClickableItem {

    @Getter private final ItemStack item;
    @Getter private final boolean canPickUp;
    private final Consumer<InventoryClickEvent> consumer;

    private ClickableItem(ItemStack item, boolean canPickUp, Consumer<InventoryClickEvent> consumer) {
        this.item = item;
        this.canPickUp = canPickUp;
        this.consumer = consumer;
    }

    public static ClickableItem empty(ItemStack item, boolean canPickUp) {
        return new ClickableItem(item, canPickUp, inventoryClickEvent -> {});
    }

    public static ClickableItem of(ItemStack item, boolean canPickUp, Consumer<InventoryClickEvent> consumer) {
        return new ClickableItem(item, canPickUp, consumer);
    }

    public void run(InventoryClickEvent e) { consumer.accept(e); }

}
