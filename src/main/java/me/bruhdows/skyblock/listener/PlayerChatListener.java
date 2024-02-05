package me.bruhdows.skyblock.listener;

import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.util.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("deprecation")
public record PlayerChatListener(SkyblockPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        CachedMetaData metaData = plugin.getLuckPerms().getPlayerAdapter(Player.class).getMetaData(player);
        String group = metaData.getPrimaryGroup();
        String groupFormat = plugin.getChat().getGroupsFormat().get(group);
        String format = (groupFormat != null ? groupFormat : plugin.getChat().getChatFormat())
                .replace("{NAME}", player.getName())
                .replace("{DISPLAYNAME}", player.getDisplayName())
                .replace("{PREFIX}", (metaData.getPrefix() != null) ? metaData.getPrefix() : "")
                .replace("{SUFFIX}", (metaData.getSuffix() != null) ? metaData.getSuffix() : "");
        format = TextUtil.color(PlaceholderAPI.setPlaceholders(player, format));

        e.setFormat(format.replace("{MESSAGE}", (player.hasPermission("skyblock.chat.colors") && player.hasPermission("skyblock.chat.rgbcolors")) ?
                TextUtil.color(message) : (player.hasPermission("skyblock.chat.colors") ? TextUtil.legacyColor(message) : (player.hasPermission("skyblock.chat.rgbcolors") ?
                TextUtil.color(message) : message))).replace("%", "%%"));
    }
}
