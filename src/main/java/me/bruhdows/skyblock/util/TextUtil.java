package me.bruhdows.skyblock.util;

import me.bruhdows.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class TextUtil {

    public static Component miniMessage(String message) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
        return serializer.deserialize(color(message));
    }

    public static String color(String message) {
        Pattern pattern = Pattern.compile("&(#[a-fA-F0-9]{6})");
        for (Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
            String color = message.substring(matcher.start() + 1, matcher.end());
            message = message.replace("&" + color, String.valueOf(ChatColor.of(color)));
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String legacyColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(miniMessage(message));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static void sendMessages(Player player, List<String> messages) {
        messages.forEach(message -> sendMessage(player, message));
    }

    public static void sendMessages(CommandSender sender, List<String> messages) {
        messages.forEach(message -> sendMessage(sender, message));
    }

    public static void sendMessages(Player player, String... messages) {
        Arrays.stream(messages).forEach(message -> sendMessage(player, message));
    }

    public static void sendMessages(CommandSender sender, String... messages) {
        Arrays.stream(messages).forEach(message -> sendMessage(sender, message));
    }

    public static void sendActionbar(Player player, String message) {
        player.sendActionBar(miniMessage(message));
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 20, 80, 20);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(color(title), color(subtitle), fadeIn, stay, fadeOut);
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(color(message));
    }

    public static void broadcastMessage(String permission, String message) {
        Bukkit.broadcast(color(message), permission);
    }

    public static void broadcastMessages(List<String> messages) {
        messages.forEach(TextUtil::broadcastMessage);
    }

    public static void broadcastMessages(String permission, List<String> messages) {
        messages.forEach(message -> broadcastMessage(permission, message));
    }

    public static void broadcastMessages(String... messages) {
        Arrays.stream(messages).forEach(TextUtil::broadcastMessage);
    }

    public static void broadcastMessages(String permission, String... messages) {
        Arrays.stream(messages).forEach(message -> broadcastMessage(permission, message));
    }

    public static void broadcastTitle(String title, String subtitle) {
        Bukkit.getOnlinePlayers().forEach(player -> sendTitle(player, title, subtitle));
    }

    public static void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> sendTitle(player, title, subtitle, fadeIn, stay, fadeOut));
    }

    public static void broadcastActionbar(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendActionbar(player, message));
    }

    public static void log(Level level, String message) {
        SkyblockPlugin.getInstance().getLogger().log(level, color(message));
    }

    public static void log(Level level, String prefix, String message) {
        Bukkit.getLogger().log(level, color(prefix) + " " + color(message));
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void severe(String message) {
        log(Level.SEVERE, message);
    }

    public static void warn(String message) {
        log(Level.WARNING, message);
    }

    public static void info(String prefix, String message) {
        log(Level.INFO, prefix, message);
    }

    public static void severe(String prefix, String message) {
        log(Level.SEVERE, prefix, message);
    }

    public static void warn(String prefix,  String message) {
        log(Level.WARNING, prefix, message);
    }
}
