package me.bruhdows.skyblock.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.command.CommandSender;

public class MissingPermissionsHandler implements dev.rollczi.litecommands.permission.MissingPermissionsHandler<CommandSender> {

    private final SkyblockPlugin plugin;

    public MissingPermissionsHandler(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        String permissions = missingPermissions.asJoinedText();
        CommandSender sender = invocation.sender();

        TextUtil.sendMessage(sender, plugin.getMessages().getNoPermission().replace("{PERMISSION}", permissions));
    }
}