package me.bruhdows.skyblock.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.entity.Player;

@Command(name = "item")
public class ItemCommand {

    @Execute(name = "get")
    public void getItem(@Context Player player, @Arg("itemId") String id) {
        Item item = SkyblockPlugin.getInstance().getItemManager().getItem(id);

        if (item == null) {
            TextUtil.sendMessage(player, "&cItem not found.");
            return;
        }

        TextUtil.sendMessage(player, "&aAdded item to your inventory.");
        player.getInventory().addItem(item.getItem());
    }

}
