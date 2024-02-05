package me.bruhdows.skyblock.core.mob;

import lombok.Getter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.core.item.StatType;
import me.bruhdows.skyblock.util.ColorUtil;
import me.bruhdows.skyblock.util.RandomUtil;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;

@Getter
@SuppressWarnings("deprecation")
public abstract class Mob {

    private final String id;
    private final String name;
    private final int maxHealth;
    private final EntityType entityType;
    private final EnumMap<StatType, Integer> stats;
    private final Map<Double, MobLoot> lootTable;

    public Mob(String id,
               String name,
               int maxHealth,
               EntityType entityType,
               EnumMap<StatType, Integer> stats,
               Map<Double, MobLoot> lootTable) {
        this.id = id;
        this.name = name;
        this.maxHealth = maxHealth;
        this.entityType = entityType;
        this.stats = stats;
        this.lootTable = lootTable;
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

    public void death(Player attacker, LivingEntity entity) {
        this.lootTable.forEach((chance, loot) -> {
            if (RandomUtil.chanceOf(chance)) {
                int randomAmount = RandomUtil.randomInt(loot.getMinAmount(), loot.getMaxAmount());
                ItemStack item = loot.getLoot().getItem();
                item.setAmount(randomAmount);
                entity.getWorld().dropItem(entity.getLocation(), item);
                if (randomAmount > 1) {
                    TextUtil.sendMessage(attacker, ColorUtil.getColorHex(loot.getLoot().getRarity().color) + "&l" + loot.getLoot().getRarity().name() + " DROP! &fYou dropped &ax" + randomAmount + " " + loot.getLoot().getItem().getItemMeta().getDisplayName());
                    return;
                }
                TextUtil.sendMessage(attacker, ColorUtil.getColorHex(loot.getLoot().getRarity().color) + "&l" + loot.getLoot().getRarity().name() + " DROP! &fYou dropped " + loot.getLoot().getItem().getItemMeta().getDisplayName());
            }
        });
        onDeath(attacker, entity);
    }

    public void damage(Player attacker, LivingEntity entity) {
        onDamage(attacker, entity);
    }

    public void decreaseHealth(Player player, int value, LivingEntity living) {
        if (value >= living.getHealth()) {
            SkyblockPlugin.getInstance().getMobManager().removeMob(living.getUniqueId());
            death(player, living);
            living.remove();
            return;
        }

        living.setHealth(living.getHealth() - value);
        living.setCustomName(TextUtil.color(getName() + " &a" + (int) living.getHealth() + "&7/&a" + getMaxHealth()));
    }

    protected abstract void onSpawn(LivingEntity entity);
    protected abstract void onDeath(Player attacker, LivingEntity entity);
    protected abstract void onDamage(Player attacker, LivingEntity entity);

}
