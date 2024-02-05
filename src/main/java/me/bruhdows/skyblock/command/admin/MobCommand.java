package me.bruhdows.skyblock.command.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.bruhdows.skyblock.core.mob.Mob;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Command(name = "mob")
@Permission("skyblock.command.mob")
public class MobCommand {

    @Execute(name = "spawn")
    public void spawn(@Context Player player, @Arg("mobId") Mob mob, @Arg("amount") Optional<Integer> amount) {
        Mob newMob;
        try {
            newMob = mob.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < amount.orElse(1); i++) {
            newMob.spawn(player.getLocation());
        }
        TextUtil.sendMessage(player, "&aMob spawned.");
    }
}
