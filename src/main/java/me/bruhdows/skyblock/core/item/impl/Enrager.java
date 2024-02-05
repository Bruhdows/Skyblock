package me.bruhdows.skyblock.core.item.impl;

import me.bruhdows.skyblock.core.item.Item;
import me.bruhdows.skyblock.core.item.ItemType;
import me.bruhdows.skyblock.core.item.Rarity;
import me.bruhdows.skyblock.core.item.StatType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Enrager extends Item {

    public Enrager() {
        super("ENRAGER",
                new ItemStack(Material.IRON_AXE),
                ItemType.AXE,
                new EnumMap<>(Map.of(
                        StatType.DAMAGE, 200,
                        StatType.STRENGTH, 300,
                        StatType.CRIT_CHANCE, 50,
                        StatType.CRIT_DAMAGE, 100
                )),
                List.of("LEFT_CLICK_ABILITY"),
                Rarity.EPIC
        );
    }
}
