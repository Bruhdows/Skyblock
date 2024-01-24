package me.bruhdows.skyblock.task;

import lombok.RequiredArgsConstructor;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.item.StatType;
import me.bruhdows.skyblock.module.user.User;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class StatsTask extends BukkitRunnable {

    private final SkyblockPlugin plugin;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = plugin.getUserManager().getUser(player);
            TextUtil.sendActionbar(player,
                    "&c" + (int) user.getStat(StatType.HEALTH) + "/" + user.getMaxStat(StatType.HEALTH) +
                    " &a" + user.getMaxStat(StatType.DEFENSE) + " Defense");
        });
    }
}
