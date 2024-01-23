package me.bruhdows.skyblock.module.item.impl;

import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.module.item.ItemType;
import me.bruhdows.skyblock.module.item.Rarity;
import me.bruhdows.skyblock.util.item.ItemBuilder;
import org.bukkit.Material;

public class EnchantedDiamond extends Item {
    public EnchantedDiamond() {
        super(
                "ENCHANTED_DIAMOND",
                ItemBuilder.of(Material.DIAMOND).name("Enchanted Diamond").get(),
                ItemType.ITEM,
                null,
                null,
                Rarity.RARE
        );
    }
}
