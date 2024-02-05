package me.bruhdows.skyblock.util;

import me.bruhdows.arena.BoxhuntArena;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class TextUtil {

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
        player.sendMessage(color(message));
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
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(color(message)));
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
        BoxhuntArena.getInstance().getLogger().log(level, color(message));
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
