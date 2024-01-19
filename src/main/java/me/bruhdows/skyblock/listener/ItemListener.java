package me.bruhdows.skyblock.listener;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableItemNBT;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.module.item.ItemType;
import me.bruhdows.skyblock.module.item.Rarity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public record ItemListener(SkyblockPlugin plugin) implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        ItemStack is = e.getItem();
        if (is == null) return;

        String id = NBT.get(is, (Function<ReadableItemNBT, String>) nbt -> nbt.getString("ID"));
        if (e.getHand() == null) return;
        if (id == null || id.isEmpty()) {
            Item newItem = new Item(e.getItem().getType().toString(), is, ItemType.ITEM, null, null, Rarity.COMMON);
            if (!newItem.matches(is)) player.getInventory().setItem(e.getHand(), newItem.getItem());
        }

        Item item = plugin.getItemManager().getItem(id);
        if (item == null) return;
        if (!item.matches(is)) player.getInventory().setItem(e.getHand(), item.getItem());

        item.getAbilities().forEach(ability -> ability.use(player, e.getAction()));
    }
}
