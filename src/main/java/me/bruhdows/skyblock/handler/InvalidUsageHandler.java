package me.bruhdows.skyblock.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.command.CommandSender;

public class InvalidUsageHandler implements dev.rollczi.litecommands.invalidusage.InvalidUsageHandler<CommandSender> {

    private final SkyblockPlugin plugin;

    public InvalidUsageHandler(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        CommandSender sender = invocation.sender();
        Schematic schematic = result.getSchematic();

        if (schematic.isOnlyFirst()) {
            TextUtil.sendMessage(sender, plugin.getMessages().getUsage().replace("{USAGE}", schematic.first()));
            return;
        }

        TextUtil.sendMessage(sender, plugin.getMessages().getUsage().replace("{USAGE}", ""));
        schematic.all().forEach(s -> TextUtil.sendMessage(sender, plugin.getMessages().getUsageList().replace("{USAGE}", s)));
    }
}