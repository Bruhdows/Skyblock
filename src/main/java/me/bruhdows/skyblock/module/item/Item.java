package me.bruhdows.skyblock.module.item;

import de.tr7zw.nbtapi.NBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.bruhdows.skyblock.module.ability.Ability;
import me.bruhdows.skyblock.module.ability.AbilityCostType;
import me.bruhdows.skyblock.module.ability.AbilityCostValueType;
import me.bruhdows.skyblock.util.ColorUtil;
import me.bruhdows.skyblock.util.item.ItemBuilder;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Item {

    private String id;
    private ItemStack itemStack;
    private ItemType type;
    private EnumMap<StatType, Integer> stats;
    private List<Ability> abilities;
    private Rarity rarity;

    public ItemStack getItem() {
        ItemStack is = itemStack.clone();

        NBT.modify(is, nbt -> {
            nbt.setString("ID", id);
        });

        ItemBuilder itemBuilder = ItemBuilder.of(is);
        itemBuilder.flag(ItemFlag.HIDE_ATTRIBUTES);

        if (is.hasItemMeta() && !is.getItemMeta().getDisplayName().contains("§")) itemBuilder.name(ColorUtil.getColorHex(rarity.color) + (is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : WordUtils.capitalizeFully(is.getType().name().replace("_", " "))));


        if (stats != null) {
            stats.forEach((statType, statValue) -> {
                itemBuilder.appendLore("&7" + statType.getName() + ": " + ColorUtil.getColorHex(statType.getColor()) + "+" + statValue);
            });
            itemBuilder.appendLore("");
        }

        if (abilities != null) {
            abilities.forEach(ability -> {
                itemBuilder.appendLore("&6Ability: " + ability.getName() + " " + ColorUtil.getColorHex(ability.getTriggerType().color) + "&l" + ability.getTriggerType().name);
                if (ability.getDescription() != null) itemBuilder.appendLore(ability.getDescription());
                if (ability.getAbilityCosts() != null) ability.getAbilityCosts().forEach(abilityCost -> {
                    itemBuilder.appendLore("&8" + abilityCost.getType().name + " Cost: " + ColorUtil.getColorHex(abilityCost.getType().color) + integerOrDouble(abilityCost.getValue()) + (abilityCost.getValueType().equals(AbilityCostValueType.PRECENT) ? "%" : ""));
                });
                itemBuilder.appendLore("");
            });
        }

        itemBuilder.appendLore(ColorUtil.getColorHex(rarity.color) + "&l" + rarity.name() + " " + type);
        return itemBuilder.get();
    }

    public boolean matches(ItemStack item) {
        return getItem().getItemMeta().getLore().equals(item.getItemMeta().getLore());
    }

    public Object integerOrDouble(double value) {
        if (value == (int) value) return (int) value;
        return value;
    }
}