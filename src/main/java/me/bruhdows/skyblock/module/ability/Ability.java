package me.bruhdows.skyblock.module.ability;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Ability {

    private final String id;
    private final String name;
    private final List<String> description;
    private final Duration cooldown;
    private final Set<AbilityCost> abilityCosts;
    private final AbilityTriggerType triggerType;

    private long lastUse;

    private AbilityTriggerType getTriggerType(Player player, Action action) {
        if (action.isLeftClick()) {
            if (player.isSneaking()) return AbilityTriggerType.LEFT_CLICK_SHIFT;
            else return AbilityTriggerType.LEFT_CLICK;
        } else if (action.isRightClick()) {
            if (player.isSneaking()) return AbilityTriggerType.RIGHT_CLICK_SHIFT;
            else return AbilityTriggerType.RIGHT_CLICK;
        } else if (player.isSneaking()) return AbilityTriggerType.SHIFT;
        return null;
    }

    public void use(Player player, Action action) {
        if (getTriggerType(player, action) != triggerType) return;
        setLastUse(System.currentTimeMillis());
        onUse(player);
    }

    protected abstract void onUse(Player player);

}
