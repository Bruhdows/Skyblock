package me.bruhdows.skyblock.module.mob.impl;

import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.item.StatType;
import me.bruhdows.skyblock.module.mob.Mob;
import me.bruhdows.skyblock.module.mob.MobLoot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.EnumMap;
import java.util.Map;

public class Dummy extends Mob {

    public Dummy() {
        super(
                "DUMMY",
                "&6Dummy",
                new EnumMap<>(Map.of(
                        StatType.DEFENSE, 10
                )),
                1000,
                Map.of(50.0, new MobLoot(SkyblockPlugin.getInstance().getItemManager().getItem("ENCHANTED_DIAMOND"), 1, 3)),
                EntityType.ZOMBIE
        );
    }

    @Override
    public void onSpawn(LivingEntity entity) {
        entity.setAI(false);
    }

    @Override
    protected void onDeath(LivingEntity entity) {

    }
}
