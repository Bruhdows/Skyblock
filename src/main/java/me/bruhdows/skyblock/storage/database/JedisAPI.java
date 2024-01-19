/*
 * Decompiled with CFR 0.150.
 */
package me.bruhdows.skyblock.storage.database;

import lombok.Getter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.storage.config.section.RedisSection;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class JedisAPI {

    private Jedis jedis;
    private JedisPool pool;
    private final RedisSection redisSection;

    public JedisAPI(RedisSection redisSection) {
        this.redisSection = redisSection;
    }

    public void connect() {
        try {
            if (!redisSection.getUrl().isEmpty()) pool = new JedisPool(redisSection.getUrl());
            else {
                String[] split = redisSection.getAddress().split(":");
                pool = new JedisPool(split[0], Integer.parseInt(split[1]));
            }
            jedis = pool.getResource();
            jedis.auth(redisSection.getPassword());
            TextUtil.info("&a[Redis]", "Successfully connected.");
        } catch (Exception e) {
            TextUtil.severe("&c[Redis]", "An error occurred while connecting.");
            TextUtil.severe("&c[Redis]", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(SkyblockPlugin.getInstance());
        }
    }

    public void disconnect() {
        if (jedis != null) pool.close();
    }
}

