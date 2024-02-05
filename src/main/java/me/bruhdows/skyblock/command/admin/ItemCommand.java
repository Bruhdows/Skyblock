package me.bruhdows.skyblock.command.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.bruhdows.skyblock.core.item.Item;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.entity.Player;

@Command(name = "item", aliases = "i")
@Permission("skyblock.command.item")
public class ItemCommand {

    @Execute(name = "get")
    public void getItem(@Context Player player, @Arg("item") Item item) {
        TextUtil.sendMessage(player, "&aAdded item to your inventory.");
        player.getInventory().addItem(item.getItem());
    }

}
