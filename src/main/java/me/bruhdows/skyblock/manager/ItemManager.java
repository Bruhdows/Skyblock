package me.bruhdows.skyblock.manager;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableItemNBT;
import lombok.Getter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

    public Item getPlayerItem(Player player) {
        ItemStack is = player.getInventory().getItem(EquipmentSlot.HAND);
        if (is.getType() == Material.AIR) return null;

        String id = NBT.get(is, (Function<ReadableItemNBT, String>) nbt -> nbt.getString("ID"));
        return SkyblockPlugin.getInstance().getItemManager().getItem(id);
    }
}
