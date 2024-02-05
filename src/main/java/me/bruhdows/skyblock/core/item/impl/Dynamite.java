package me.bruhdows.skyblock.core.item.impl;

import me.bruhdows.skyblock.core.item.Item;
import me.bruhdows.skyblock.core.item.ItemType;
import me.bruhdows.skyblock.core.item.Rarity;
import me.bruhdows.skyblock.core.item.StatType;
import me.bruhdows.skyblock.util.item.ItemBuilder;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Dynamite extends Item {

    public Dynamite() {
        super("DYNAMITE",
                ItemBuilder.of(Material.TNT).name("Dynamite").get(),
                ItemType.SWORD,
                new EnumMap<>(Map.of(
                        StatType.DAMAGE, 20,
                        StatType.STRENGTH, 100)),
                List.of("EXPLOSION"),
                Rarity.SPECIAL);
    }
}
