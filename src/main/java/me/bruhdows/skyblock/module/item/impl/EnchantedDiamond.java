package me.bruhdows.skyblock.module.item.impl;

import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.module.item.ItemType;
import me.bruhdows.skyblock.module.item.Rarity;
import me.bruhdows.skyblock.module.item.StatType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EnchantedDiamond extends Item {
    public EnchantedDiamond() {
        super("ENCHANTED_DIAMOND", new ItemStack(Material.DIAMOND), ItemType.ITEM, null, List.of(), Rarity.RARE);
    }
}
