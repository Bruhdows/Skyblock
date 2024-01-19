package me.bruhdows.skyblock.manager;

import lombok.Getter;
import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.util.TextUtil;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ItemManager {

    private final Map<String, Item> items = new HashMap<>();

    public void registerItem(Item item) {
        if (items.containsKey(item.getId())) {
            TextUtil.warn("Duplicate item found! Skipping. (" + item.getId() + ")");
            return;
        }
        items.put(item.getId(), item);
    }

    public Item getItem(String id) {
        return items.get(id);
    }
}
