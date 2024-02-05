package me.bruhdows.skyblock.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.deathMessage(null);
        e.setKeepInventory(true);
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.getEntity().spigot().respawn();
        e.getEntity().updateInventory();
    }
}
