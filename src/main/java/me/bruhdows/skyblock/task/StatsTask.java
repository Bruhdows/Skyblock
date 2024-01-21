package me.bruhdows.skyblock.task;

import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;

public class StatsTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            TextUtil.sendActionbar(player, "&#");
        });
    }
}
