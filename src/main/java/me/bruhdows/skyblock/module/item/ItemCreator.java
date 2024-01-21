package me.bruhdows.skyblock.module.item;

import me.bruhdows.skyblock.module.ability.Ability;

public class ItemCreator {

    private final Item item;

    public ItemCreator(Item item) {
        this.item = item;
    }

    public static ItemCreator of(Item item) {
        return new ItemCreator(item);
    }

    public void addStat(StatType type, int amount) {
        item.getStats().put(type, amount);
    }

    public void removeStat(StatType type) {
        item.getStats().remove(type);
    }

    public void addAbility(Ability ability) {
        item.getAbilities().add(ability.getId());
    }

    public void removeAbility(Ability ability) {
        item.getAbilities().remove(ability.getId());
    }

    public void setRarity(Rarity rarity) {
        item.setRarity(rarity);
    }

    public void setItemType(ItemType type) {
        item.setType(type);
    }
}
