package me.bruhdows.skyblock.core.mob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.bruhdows.skyblock.core.item.Item;

@Getter
@AllArgsConstructor
public class MobLoot {

    private Item loot;
    private int minAmount;
    private int maxAmount;

}
