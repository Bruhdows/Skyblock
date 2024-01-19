/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.bruhdows.skyblock.storage.database;

import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.manager.JedisManager;
import me.bruhdows.skyblock.module.user.User;
import me.bruhdows.skyblock.util.SerializationUtil;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class JedisListener extends JedisPubSub {

    private final SkyblockPlugin instance;

    public JedisListener(SkyblockPlugin instance) {
        this.instance = instance;
    }

    @Override
    public void onMessage(String channel, String originalData) {
        String[] data = JedisManager.convertMessage(originalData);
        String serverId = data[0];
        if (serverId.equals(instance.getData().getServerId())) return;
        switch (data[1]) {
            case "player": {
                UUID uuid = UUID.fromString(data[2]);
                User user = (User) SerializationUtil.deserializeObject(data[3]);
                if (user == null) return;
                user.setUpdate(false);
                this.instance.getUserManager().getUsers().remove(this.instance.getUserManager().getUser(uuid));
                this.instance.getUserManager().getUsers().add(user);
                Player player = user.getPlayer();
                if (this.instance.getConfiguration().isDebug())
                    TextUtil.info("&9[Debug]", "&fReceived user update &7(" + user.getName() + ")");
                if (player != null) {
                    Bukkit.getScheduler().runTaskLater(this.instance, () -> {
                        if (this.instance.getConfiguration().isDebug())
                            TextUtil.info("&9[Debug]", "&fSyncing user update &7(" + user.getName() + ")");
                        user.syncPlayer(player);
                        if (this.instance.getConfiguration().isDebug()) TextUtil.info(Arrays.toString(user.getInventory().getContents()));
                    }, 1L);
                }
                break;
            }
            case "broadcast": {
                Bukkit.broadcastMessage(data[2]);
                break;
            }
        }
    }
}

