package me.bruhdows.skyblock.listener;

import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.user.User;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record JoinQuitListener(SkyblockPlugin plugin) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.joinMessage(null);

        Player player = e.getPlayer();
        User user = this.plugin.getUserManager().getUser(player);
        if (user != null) {
            if (!user.getName().equals(player.getName())) user.setNick(player.getName());
            user.syncPlayer(player);
            if (this.plugin.getConfiguration().isDebug()) TextUtil.info("&9[Debug]", "&fSyncing " + user.getName() + " data");
            user.setLastServerId(plugin.getData().getServerId());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                User newUser = this.plugin.getUserManager().createUser(player);
                newUser.setLastServerId(plugin.getData().getServerId());
            });
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.quitMessage(null);

        Player player = e.getPlayer();
        User user = this.instance.getUserManager().getUser(player);
        if (user == null) return;
        user.setLastServerId(instance.getData().getServerId());
        user.updateData(player);
    }
}
