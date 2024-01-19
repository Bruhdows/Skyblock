package me.bruhdows.skyblock.util.item;

import lombok.NonNull;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class ItemBuilder {

    private ItemStack itemStack;

    private ItemBuilder() {
    }

    private ItemBuilder(@NonNull Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public static ItemBuilder of(@NonNull Material material) {
        return new ItemBuilder(material, 1);
    }

    public static ItemBuilder of(@NonNull Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemBuilder of(@NonNull ItemStack item) {
        return ItemBuilder.of(item.getType(), item.getAmount())
                .durability(item.getDurability())
                .itemMeta(item.getItemMeta());
    }

    public static ItemBuilder ofCopy(@NonNull ItemStack item) {
        ItemBuilder ItemBuilder = new ItemBuilder();
        ItemBuilder.itemStack = item.clone();
        return ItemBuilder;
    }

    public ItemBuilder name(@NonNull String displayName) {
        return this.rawName(TextUtil.color(displayName));
    }

    public ItemBuilder rawName(@NonNull String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder lore(@NonNull String lore) {
        return this.lore(Collections.singletonList(lore));
    }

    public ItemBuilder lore(@NonNull List<String> lore) {
        return this.rawLore(lore.stream()
                .map(TextUtil::color)
                .collect(Collectors.toList()));
    }

    public ItemBuilder lore(@NonNull String... lore) {
        return this.rawLore(Stream.of(lore)
                .map(TextUtil::color)
                .collect(Collectors.toList()));
    }

    public ItemBuilder rawLore(@NonNull String lore) {
        return this.rawLore(Collections.singletonList(lore));
    }

    public ItemBuilder rawLore(@NonNull List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(lore);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder rawLore(@NonNull String... lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(List.of(lore));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder appendRawLore(@NonNull List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (!itemMeta.hasLore()) {
            itemMeta.setLore(lore);
        } else {
            List<String> newLore = itemMeta.getLore();
            newLore.addAll(lore);
            itemMeta.setLore(newLore);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder appendRawLore(@NonNull String... lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (!itemMeta.hasLore()) {
            itemMeta.setLore(List.of(lore));
        } else {
            List<String> newLore = itemMeta.getLore();
            newLore.addAll(List.of(lore));
            itemMeta.setLore(newLore);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder appendLore(@NonNull List<String> lore) {
        return this.appendRawLore(lore.stream()
                .map(TextUtil::color)
                .collect(Collectors.toList()));
    }

    public ItemBuilder appendLore(@NonNull String... lore) {
        return this.appendRawLore(Stream.of(lore)
                .map(TextUtil::color)
                .collect(Collectors.toList()));
    }

    public ItemBuilder appendRawLore(@NonNull String line) {
        return this.appendRawLore(Collections.singletonList(line));
    }

    public ItemBuilder appendLore(@NonNull String line) {
        return this.appendLore(Collections.singletonList(line));
    }

    public ItemBuilder durability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder flag(@NonNull ItemFlag itemFlag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder flags(@NonNull ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder enchant(@NonNull Enchantment enchantment, int level) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder enchants(@NonNull Map<Enchantment, Integer> enchantments) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder itemMeta(@NonNull ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder unbreakable() {
        return this.unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean state) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setUnbreakable(state);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder manipulate(@NonNull ItemManipulator manipulator) {
        this.itemStack = manipulator.manipulate(this.itemStack);
        return this;
    }

    public ItemStack get() {
        return this.itemStack;
    }
}