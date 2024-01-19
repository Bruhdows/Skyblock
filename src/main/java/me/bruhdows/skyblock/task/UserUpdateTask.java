package me.bruhdows.skyblock.task;

import me.bruhdows.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UserUpdateTask extends BukkitRunnable {

    private final SkyblockPlugin skyblock;

    public UserUpdateTask(SkyblockPlugin skyblock) {
        this.skyblock = skyblock;
    }

    @Override
    public void run() {
        skyblock.getUserManager().saveUsers();
    }
}
