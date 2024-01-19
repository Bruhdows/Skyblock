package me.bruhdows.skyblock.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.module.user.User;
import me.bruhdows.skyblock.module.user.UserInventory;
import me.bruhdows.skyblock.util.SerializationUtil;
import me.bruhdows.skyblock.util.TextUtil;
import org.bson.Document;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserManager {

    private final SkyblockPlugin plugin;
    private Set<User> users;

    public UserManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadUsers() {
        this.users = new HashSet<>();
        plugin.getMongoDB().getCollection("users").find().forEach(document -> {
            String inventory = document.getString("inventory");
            users.add(new User(
                    UUID.fromString(document.getString("uuid")),
                    document.getString("name"),
                    (GameMode) SerializationUtil.deserializeObject(document.getString("gameMode")),
                    SerializationUtil.deserializeLocation(document.getString("location")),
                    inventory == null ? null : (UserInventory) SerializationUtil.deserializeObject(inventory),
                    (Map) SerializationUtil.deserializeObject(document.getString("additionalData")),
                    (boolean[]) SerializationUtil.deserializeObject(document.getString("settings"))));
        });
    }

    public void saveUsers() {
        MongoCollection<Document> collection = plugin.getMongoDB().getCollection("users");
        List<User> users = plugin.getUserManager().getUsers().stream().filter(User::isUpdate).toList();
        users.forEach(user -> {
            collection.replaceOne(Filters.eq("uuid", user.getUuid().toString()), user.createDocument());
            user.setUpdate(false);
            if (plugin.getConfiguration().isDebug()) TextUtil.info("&9[Debug]", "&fSaved " + user.getName() + " data.");
        });
        if (!users.isEmpty()) TextUtil.info("&9[Debug]", "&fSaved " + users.size() + " players to the database.");
    }

    public User createUser(Player player) {
        User user = new User(
                player.getUniqueId(),
                player.getName(),
                player.getGameMode(),
                player.getLocation(),
                new UserInventory(player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getEnderChest().getContents()),
                new HashMap<>(),
                new boolean[7]);
        plugin.getMongoDB().getCollection("users").insertOne(user.createDocument());
        users.add(user);
        user.sendUpdate(plugin);
        return user;
    }

    public User getUser(Player player) {
        for (User user : users) {
            if (!user.getUuid().equals(player.getUniqueId())) continue;
            return user;
        }
        return null;
    }

    public User getUser(UUID uuid) {
        for (User user : users) {
            if (!user.getUuid().equals(uuid)) continue;
            return user;
        }
        return null;
    }
}