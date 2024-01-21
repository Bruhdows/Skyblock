package me.bruhdows.skyblock.module.mob.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.bruhdows.skyblock.module.item.Item;

@Getter
@AllArgsConstructor
public class MobLoot {

    private Item loot;
    private int minAmount;
    private int maxAmount;

}
