package me.bruhdows.skyblock.module.user;

import de.tr7zw.nbtapi.NBTContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.manager.JedisManager;
import me.bruhdows.skyblock.util.SerializationUtil;
import me.bruhdows.skyblock.util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.checkerframework.checker.units.qual.N;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class User implements Serializable {

    private UUID uuid;
    private String name;
    private GameMode gameMode;
    private Location location;
    private UserInventory inventory;
    private Map<String, UserData> data;
    private boolean[] settings;

    private boolean update;
    private String lastServerId;

    public User(UUID uuid,
                String name,
                GameMode gameMode,
                Location location,
                UserInventory inventory,
                Map<String, UserData> data,
                boolean[] settings) {
        this.uuid = uuid;
        this.name = name;
        this.gameMode = gameMode;
        this.location = location;
        this.inventory = inventory;
        this.data = data;
        this.settings = Arrays.copyOf(settings, 8);
    }

    public Document createDocument() {
        Document document = new Document();
        document.put("uuid", this.uuid.toString());
        document.put("name", this.name);
        document.put("gameMode", SerializationUtil.serializeObject(this.gameMode));
        document.put("location", SerializationUtil.serializeLocation(this.location));
        document.put("inventory", SerializationUtil.serializeObject(this.inventory));
        HashMap<String, UserData> userDataHashMap = new HashMap<>(this.data);
        this.data.forEach((string, data) -> {
            if (data.isSave()) userDataHashMap.put(string, data);
        });
        document.put("data", SerializationUtil.serializeObject(userDataHashMap));
        document.put("settings", SerializationUtil.serializeObject(this.settings));
        return document;
    }

    public void syncPlayer(Player player) {
        try {
            if (this.inventory != null) {
                player.getInventory().setContents(this.inventory.getContents());
                player.getInventory().setArmorContents(this.inventory.getArmorContents());
                player.getEnderChest().setContents(this.inventory.getEnderContents());
            }
            if (this.gameMode != null) player.setGameMode(this.gameMode);
            if (this.location != null && !player.getLocation().equals(this.location)) player.teleport(this.location);
            player.setAllowFlight(this.settings[2]);
            player.setFlying(this.settings[3]);
        } catch (Exception e) {
            player.kick(Component.text(TextUtil.color("&cAn error occurred while loading the user.")), PlayerKickEvent.Cause.PLUGIN);
            TextUtil.severe("&cAn error occurred while loading user (" + this.name + ")");
            TextUtil.severe(e.getMessage());
        }
    }

    public void updateData(Player player) {
        this.gameMode = player.getGameMode();
        this.location = player.getLocation();
        this.settings[2] = player.getAllowFlight();
        this.settings[3] = player.isFlying();
        this.updateInventory(player, false);
        this.setUpdate(true);
    }

    public void updateInventory(Player player, boolean update) {
        if (this.inventory == null) this.inventory = new UserInventory(player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getEnderChest().getContents());
        else {
            this.inventory.setContents(player.getInventory().getContents());
            this.inventory.setArmorContents(player.getInventory().getArmorContents());
            this.inventory.setEnderContents(player.getEnderChest().getContents());
        }
        if (update) this.setUpdate(true);
    }

    public void sendUpdate(SkyblockPlugin plugin) {
        String publishMessage = JedisManager.createPublishMessage(plugin.getData().getServerId(), "player", uuid.toString(), SerializationUtil.serializeObject(this));
        if (plugin.getConfiguration().isDebug()) TextUtil.info("&9[Debug]", "&fPlayer sent data (" + this.name + ")");
        JedisManager.publish(plugin.getJedisAPI(), JedisManager.getChannel(), publishMessage);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public Location getLocation() {
        Player player = this.getPlayer();
        if (player != null) {
            return player.getLocation();
        }
        return this.location;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        this.setUpdate(true);
        Player player = this.getPlayer();
        if (player != null) {
            player.setGameMode(gameMode);
        }
    }

    public void setUpdate(boolean update) {
        this.update = update;
        if (update) this.sendUpdate(SkyblockPlugin.getInstance());
    }

    public void setNick(String name) {
        this.name = name;
        this.setUpdate(true);
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
