package me.bruhdows.skyblock.task;

import lombok.RequiredArgsConstructor;
import me.bruhdows.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class UserUpdateTask extends BukkitRunnable {

    private final SkyblockPlugin skyblock;

    @Override
    public void run() {
        skyblock.getUserManager().saveUsers();
    }
}
