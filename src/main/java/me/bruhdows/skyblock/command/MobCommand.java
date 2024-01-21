package me.bruhdows.skyblock.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import eu.okaeri.configs.annotation.Exclude;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.mob.Mob;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

@Command(name = "mob")
public class MobCommand {

    @Execute
    public void spawn(@Context Player player, @Arg("id") String id) {
        Mob mob = SkyblockPlugin.getInstance().getMobManager().getMob(id);

        if(mob == null) {
            TextUtil.sendMessage(player, "&cMob not found.");
            return;
        }

        Mob cloned;
        try {
            cloned = mob.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        TextUtil.sendMessage(player, "&aMob spawned.");
        cloned.spawn(player.getLocation());
    }
}
