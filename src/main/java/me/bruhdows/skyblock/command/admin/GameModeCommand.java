package me.bruhdows.skyblock.command.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "gamemode")
@Permission("skyblock.command.gamemode")
public class GameModeCommand {

    @Execute
    public void setGameMode(@Context Player player, @Arg GameMode gameMode) {
        player.setGameMode(gameMode);

        if(!player.hasPermission("skyblock.command.gamemode." + gameMode.name().toLowerCase())) {

        }

        TextUtil.sendMessage(player, "&7Gamemode set to &b" + gameMode.name().toLowerCase());
    }

    @Execute
    @Permission("skyblock.command.gamemode.other")
    public void setPlayerGameMode(@Context CommandSender sender, @Arg("target") Player target, @Arg("gameMode") GameMode gameMode) {
        target.setGameMode(gameMode);
        TextUtil.sendMessage(sender, "&7Set gamemode &b" + gameMode.name().toLowerCase() + " &7for &b" + target.getName());
    }
}
