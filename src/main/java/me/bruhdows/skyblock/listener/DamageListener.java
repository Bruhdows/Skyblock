package me.bruhdows.skyblock.listener;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.item.StatType;
import me.bruhdows.skyblock.module.mob.Mob;
import me.bruhdows.skyblock.module.user.User;
import me.bruhdows.skyblock.util.RandomUtil;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public record DamageListener(SkyblockPlugin plugin) implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();

        if (!(entity instanceof LivingEntity living)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        Mob mob = SkyblockPlugin.getInstance().getMobManager().getAliveMob(entity.getUniqueId());
        if (mob == null) return;

        e.setDamage(0);

        User user = plugin.getUserManager().getUser(player);
        int damage = user.calculateDamage();

        int defense = mob.getStats().get(StatType.DEFENSE);
        damage = damage * defense / (defense + 100);

        String name = living.getUniqueId() + "_" + UUID.randomUUID();
        List<String> damageHolo = List.of("&7" + damage);

        if (RandomUtil.chanceOf(user.getStat(StatType.CRIT_CHANCE))) {
            damage = (int) (damage * (1 + (user.getStat(StatType.CRIT_DAMAGE) / 100)));
            damageHolo = List.of("&e" + damage);
        }

        double randomX = RandomUtil.randomDouble(-0.8, 0.8);
        double randomZ = RandomUtil.randomDouble(-0.8, 0.8);

        Hologram hologram = DHAPI.createHologram(name, living.getLocation().add(randomX, 1.5, randomZ), damageHolo);
        hologram.setDefaultVisibleState(false);
        hologram.setShowPlayer(player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            DHAPI.removeHologram(name);
        }, 10L);

        if (damage >= living.getHealth()) {
            SkyblockPlugin.getInstance().getMobManager().removeMob(living.getUniqueId());
            mob.death(player, living);
            living.remove();
            return;
        }

        living.setHealth(living.getHealth() - damage);
        living.setCustomName(TextUtil.color(mob.getName() + " &a" + (int) living.getHealth() + "&7/&a" + mob.getMaxHealth()));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        plugin.getMobManager().removeMob(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveFromWorldEvent e) {
        plugin.getMobManager().removeMob(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        e.setCancelled(true);
    }
}
