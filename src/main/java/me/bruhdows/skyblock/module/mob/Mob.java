package me.bruhdows.skyblock.module.mob;

import de.tr7zw.nbtapi.NBT;
import lombok.Getter;
import lombok.Setter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

@Getter
@SuppressWarnings("deprecation")
public abstract class Mob {

    private final String id;
    private final String name;
    private final int maxHealth;
    private final EntityType entityType;

    public Mob(String id,
               String name,
               int maxHealth,
               EntityType entityType) {
        this.id = id;
        this.name = name;
        this.maxHealth = maxHealth;
        this.entityType = entityType;
    }

    public void spawn(Location location) {
        Entity entity = location.getWorld().spawnEntity(location, entityType);
        if (!(entity instanceof LivingEntity living)) return;
        SkyblockPlugin.getInstance().getMobManager().newMob(entity.getUniqueId(), id);
        living.setCustomNameVisible(true);
        living.setCustomName(TextUtil.color(name + " &a" + maxHealth + "&7/&a" + maxHealth));
        living.setMaxHealth(maxHealth);
        living.setHealth(maxHealth);
        onSpawn(living);
    }

    protected abstract void onSpawn(LivingEntity entity);
}
