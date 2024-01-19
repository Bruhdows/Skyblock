package me.bruhdows.skyblock.module.ability.impl;

import me.bruhdows.skyblock.module.ability.*;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class RightClickAbility extends Ability {

    public RightClickAbility() {
        super(
                "RIGHT_CLICK_ABILITY",
                "Right Click",
                List.of(
                        "&7This is an right click ability",
                        "&aSome more text"
                ),
                Duration.ofSeconds(1),
                Set.of(new AbilityCost(AbilityCostType.HEALTH, AbilityCostValueType.AMOUNT, 5)),
                AbilityTriggerType.RIGHT_CLICK
        );
    }

    @Override
    public void onUse(Player player) {
        TextUtil.broadcastMessage(player.getName() + " used " + this.getName() + " ability!");
    }
}
