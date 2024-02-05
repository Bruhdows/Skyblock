package me.bruhdows.skyblock.command.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import me.bruhdows.skyblock.util.TextUtil;
import org.bukkit.command.CommandSender;

public class MissingPermissionsHandler implements dev.rollczi.litecommands.permission.MissingPermissionsHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        String permissions = missingPermissions.asJoinedText();
        CommandSender sender = invocation.sender();

        TextUtil.sendMessage(sender, "&cMissing permissions: &f" + permissions);
    }
}