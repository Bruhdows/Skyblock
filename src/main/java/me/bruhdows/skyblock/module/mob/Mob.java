package me.bruhdows.skyblock.module.mob;

import lombok.Getter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.module.mob.impl.MobLoot;
import me.bruhdows.skyblock.util.ColorUtil;
import me.bruhdows.skyblock.util.RandomUtil;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@SuppressWarnings("deprecation")
public abstract class Mob {

    private final String id;
    private final String name;
    private final int maxHealth;
    private final EntityType entityType;
    private final Map<Double, MobLoot> lootTable;

    public Mob(String id,
               String name,
               int maxHealth,
               Map<Double, MobLoot> lootTable,
               EntityType entityType) {
        this.id = id;
        this.name = name;
        this.maxHealth = maxHealth;
        this.lootTable = lootTable;
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

    public void death(Player killer, LivingEntity entity) {
        this.lootTable.forEach((chance, loot) -> {
            double random = RandomUtil.randomDouble(0, 100);
            if (!(random < chance)) return;
            int randomAmount = RandomUtil.randomInt(loot.getMinAmount(), loot.getMaxAmount());
            ItemStack item = loot.getLoot().getItem();
            item.setAmount(randomAmount);
            entity.getWorld().dropItem(entity.getLocation(), item);
            if(randomAmount > 1) {
                TextUtil.sendMessage(killer, ColorUtil.getColorHex(loot.getLoot().getRarity().color) + "&l" + loot.getLoot().getRarity().name() + " DROP! &fYou dropped &ax" + randomAmount + " " + loot.getLoot().getItem().getItemMeta().getDisplayName());
                return;
            }
            TextUtil.sendMessage(killer, ColorUtil.getColorHex(loot.getLoot().getRarity().color) + "&l" + loot.getLoot().getRarity().name() + " DROP! &fYou dropped " + loot.getLoot().getItem().getItemMeta().getDisplayName());
        });
        onDeath(entity);
    }

    protected abstract void onSpawn(LivingEntity entity);

    protected abstract void onDeath(LivingEntity entity);
}
