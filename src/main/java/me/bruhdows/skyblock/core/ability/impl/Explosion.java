package me.bruhdows.skyblock.core.ability.impl;

import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.core.ability.Ability;
import me.bruhdows.skyblock.core.ability.AbilityTriggerType;
import me.bruhdows.skyblock.core.mob.Mob;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;

public class Explosion extends Ability {
    public Explosion() {
        super("EXPLOSION",
                "Explosion",
                List.of("&7Spawns an &cexplosion &7that damage", "&7mobs in range of &a10 &7blocks."),
                null,
                AbilityTriggerType.LEFT_CLICK);
    }

    @Override
    protected void onUse(Player player) {
        player.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
        player.getLocation().getNearbyLivingEntities(10).forEach(entity -> {
            Mob mob = SkyblockPlugin.getInstance().getMobManager().getAliveMob(entity.getUniqueId());
            if (mob == null) return;
            mob.decreaseHealth(player, 200, entity);
        });
    }
}
