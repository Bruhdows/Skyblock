package me.bruhdows.skyblock.module.mob.impl;

import me.bruhdows.skyblock.module.mob.Mob;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class Dummy extends Mob {

    public Dummy() {
        super(
                "DUMMY",
                "&6Dummy",
                10000,
                EntityType.ZOMBIE
        );
    }

    @Override
    public void onSpawn(LivingEntity entity) {

    }
}
