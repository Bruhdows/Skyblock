package me.bruhdows.skyblock.core.user;

import lombok.Getter;
import lombok.Setter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.storage.database.JedisManager;
import me.bruhdows.skyblock.core.item.Item;
import me.bruhdows.skyblock.core.item.StatType;
import me.bruhdows.skyblock.util.SerializationUtil;
import me.bruhdows.skyblock.util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class User implements Serializable {

    private UUID uuid;
    private String name;
    private GameMode gameMode;
    private Location location;
    private UserInventory inventory;
    private Map<String, UserData> data;
    private EnumMap<SkillType, Double> skills;
    private boolean flying;
    private boolean allowFlight;

    private boolean update;
    private String lastServerId;

    public User(UUID uuid,
                String name,
                GameMode gameMode,
                Location location,
                UserInventory inventory,
                Map<String, UserData> data,
                EnumMap<SkillType, Double> skills,
                boolean flying,
                boolean allowFlight) {
        this.uuid = uuid;
        this.name = name;
        this.gameMode = gameMode;
        this.location = location;
        this.inventory = inventory;
        this.data = data;
        this.skills = skills;
        this.flying = flying;
        this.allowFlight = allowFlight;
    }

    public Document createDocument() {
        Document document = new Document();
        document.put("uuid", this.uuid.toString());
        document.put("name", this.name);
        document.put("gameMode", SerializationUtil.serializeObject(this.gameMode));
        document.put("location", SerializationUtil.serializeLocation(this.location));
        document.put("inventory", SerializationUtil.serializeObject(this.inventory));
        HashMap<String, UserData> userData = new HashMap<>(this.data);
        this.data.forEach((string, data) -> {
            if (data.isSave()) userData.put(string, data);
        });
        document.put("data", SerializationUtil.serializeObject(userData));
        document.put("skills", SerializationUtil.serializeObject(this.skills));
        document.put("flying", this.flying);
        document.put("allowFlight", this.allowFlight);
        return document;
    }

    public void syncPlayer(Player player) {
        try {
            if (this.skills == null) skills = new EnumMap<>(SkillType.class);
            if (this.skills.size() != SkillType.values().length) {
                List<SkillType> values = Arrays.stream(SkillType.values()).collect(Collectors.toList());
                values = values.stream().filter(skillType -> !skills.containsKey(skillType)).collect(Collectors.toList());
                values.forEach(value -> skills.put(value, 0.0));
            }
            if (this.inventory != null) {
                player.getInventory().setContents(this.inventory.getContents());
                player.getInventory().setArmorContents(this.inventory.getArmorContents());
                player.getEnderChest().setContents(this.inventory.getEnderContents());
            }
            if (this.gameMode != null) player.setGameMode(this.gameMode);
            if (this.location != null && !player.getLocation().equals(this.location)) player.teleport(this.location);
            player.setAllowFlight(this.allowFlight);
            player.setFlying(this.flying);
        } catch (Exception e) {
            player.kick(Component.text(TextUtil.color("&cAn error occurred while loading the user.")), PlayerKickEvent.Cause.PLUGIN);
            TextUtil.severe("&cAn error occurred while loading user (" + this.name + ")");
            TextUtil.severe(e.getMessage());
        }
    }

    public void updateData(Player player) {
        this.gameMode = player.getGameMode();
        this.location = player.getLocation();
        this.allowFlight = player.getAllowFlight();
        this.flying = player.isFlying();
        this.updateInventory(player, false);
        this.setUpdate(true);
    }

    public void updateInventory(Player player, boolean update) {
        if (this.inventory == null)
            this.inventory = new UserInventory(player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getEnderChest().getContents());
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

    public void setLocation(Location location) {
        this.location = location;
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

    public void damage(double damage) {
        Player player = getPlayer();
        GameMode gameMode = player.getGameMode();
        if(gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) return;
        player.setHealth(Math.max(player.getHealth() - damage, 0));
    }

    public int getStat(StatType type) {
        Item item = SkyblockPlugin.getInstance().getItemManager().getPlayerItem(getPlayer());
        if (item == null) return 0;
        if (item.getStats().get(type) == null) return 0;
        return item.getStats().get(type);
    }

    public int calculateDamage() {
        int damage = 5;
        Item item = SkyblockPlugin.getInstance().getItemManager().getPlayerItem(getPlayer());
        if (item == null) return damage;
        damage = (damage + item.getStats().get(StatType.DAMAGE)) * (1 + item.getStats().get(StatType.STRENGTH) / 100);
        return damage;
    }
}
