package me.bruhdows.skyblock.core.mob.impl;

import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.core.item.StatType;
import me.bruhdows.skyblock.core.mob.Mob;
import me.bruhdows.skyblock.core.mob.MobLoot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class Dummy extends Mob {

    public Dummy() {
        super(
                "DUMMY",
                "&6Dummy",
                1000,
                EntityType.ZOMBIE,
                new EnumMap<>(Map.of(
                        StatType.DEFENSE, 100
                )),
                Map.of(50.0, new MobLoot(SkyblockPlugin.getInstance().getItemManager().getItem("ENCHANTED_DIAMOND"), 1, 3))
        );
    }

    @Override
    public void onSpawn(LivingEntity entity) {
        entity.setAI(false);
    }

    @Override
    protected void onDeath(Player attacker, LivingEntity entity) {

    }

    @Override
    protected void onDamage(Player attacker, LivingEntity entity) {

    }
}
