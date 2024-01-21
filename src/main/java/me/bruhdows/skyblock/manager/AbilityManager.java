package me.bruhdows.skyblock.manager;

import me.bruhdows.skyblock.module.ability.Ability;
import me.bruhdows.skyblock.util.TextUtil;

import java.util.HashMap;
import java.util.Map;

public class AbilityManager {

    private final Map<String, Ability> abilities = new HashMap<>();

    public void registerAbility(Ability ability) {
        if(abilities.containsValue(ability)) {
            TextUtil.warn("Duplicate ability found! Skipping. (" + ability.getId() + ")");
            return;
        }

        abilities.put(ability.getId(), ability);
    }

    public Ability getAbility(String id) {
        return abilities.get(id);
    }

}
