package me.bruhdows.skyblock.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.mob.Mob;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(name = "mob")
public class MobCommand {

    @Execute(name = "spawn")
    public void spawn(@Context Player player, @Arg("mobId") String id, @Arg("amount") Optional<Integer> amount) {
        Mob mob = SkyblockPlugin.getInstance().getMobManager().getMob(id);

        if (mob == null) {
            TextUtil.sendMessage(player, "&cMob not found.");
            return;
        }

        TextUtil.sendMessage(player, "&aMob spawned.");

        for (int i = 0; i < amount.orElse(1); i++) {
            mob.spawn(player.getLocation());
        }
    }
}
