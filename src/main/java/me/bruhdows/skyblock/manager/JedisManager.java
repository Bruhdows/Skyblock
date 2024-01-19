/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package me.bruhdows.skyblock.manager;

import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.storage.database.JedisAPI;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JedisManager {

    public static void publish(JedisAPI jedisAPI, String channel, String message) {
        try {
            if (jedisAPI.getJedis().isBroken() || !jedisAPI.getJedis().isConnected()) jedisAPI.connect();
            jedisAPI.getJedis().publish(channel, message);
        } catch (Exception e) {
            TextUtil.severe(e.getMessage());
        }
    }

    public static void subscribeChannels(JedisAPI jedisAPI, JedisPubSub sub, String... channels) {
        Jedis subJedis = jedisAPI.getPool().getResource();
        if (!jedisAPI.getRedisSection().getPassword().isEmpty()) subJedis.auth(jedisAPI.getRedisSection().getPassword());
        Thread thread = new Thread(() -> {
            try {
                subJedis.subscribe(sub, channels);
            } catch (Exception e) {
                TextUtil.severe(e.getMessage());
            }
        });
        thread.start();
    }

    public static String createPublishMessage(Object... data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object datum : data)
            stringBuilder.append(datum.toString()).append("%n");
        return stringBuilder.toString();
    }

    public static String[] convertMessage(String message) {
        return message.split("%n");
    }

    public static String getChannel() {
        return "Skyblock_" + SkyblockPlugin.getInstance().getConfiguration().getDatabase().getRedis().getChannel();
    }
}

