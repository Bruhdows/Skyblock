package me.bruhdows.skyblock.listener;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableItemNBT;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.api.color.ColorAPI;
import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.module.item.StatType;
import me.bruhdows.skyblock.module.mob.Mob;
import me.bruhdows.skyblock.util.RandomUtil;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public record DamageListener(SkyblockPlugin plugin) implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();

        if (!(entity instanceof LivingEntity living)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        Mob mob = SkyblockPlugin.getInstance().getMobManager().getAliveMob(entity.getUniqueId());
        if (mob == null) return;

        e.setDamage(0);

        int damage = calculateDamage(player);

        String name = living.getUniqueId() + "_" + UUID.randomUUID();
        List<String> damageHolo = List.of("&7" + damage);

        if (RandomUtil.chanceOf(getStat(player, StatType.CRIT_CHANCE))) {
            damage = damage * (1 + (getStat(player, StatType.CRIT_DAMAGE) / 100));
            damageHolo = List.of(ColorAPI.process("&c✧<GRADIENT:FFFF40>" + damage + "</GRADIENT:FF9000>&c✧"));
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

        living.setHealth(living.getHealth() - calculateDamage(player));
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

    public int calculateDamage(Player player) {
        int initDamage = 5;

        Item item = getItem(player);
        if (item == null) return initDamage;

        initDamage += item.getStats().get(StatType.DAMAGE) * (1 + item.getStats().get(StatType.STRENGTH) / 100);
        return initDamage;
    }

    public Item getItem(Player player) {
        ItemStack is = player.getInventory().getItem(EquipmentSlot.HAND);
        if (is.getType() == Material.AIR) return null;

        String id = NBT.get(is, (Function<ReadableItemNBT, String>) nbt -> nbt.getString("ID"));
        return plugin.getItemManager().getItem(id);
    }

    public int getStat(Player player, StatType type) {
        Item item = getItem(player);
        if (item == null) return 0;

        return item.getStats().get(type);
    }

}
