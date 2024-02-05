package me.bruhdows.skyblock.core.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.util.SerializationUtil;
import me.bruhdows.skyblock.util.TextUtil;
import org.bson.Document;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
@SuppressWarnings({"unchecked"})
public class UserManager {

    private final SkyblockPlugin plugin;
    private Set<User> users;

    public UserManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadUsers() {
        users = new HashSet<>();
        plugin.getMongoDB().getCollection("users").find().forEach(document -> {
            GameMode gameMode = document.getString("gameMode") == null ? null : (GameMode) SerializationUtil.deserializeObject(document.getString("gameMode"));
            String inventory = document.getString("inventory");
            String skills = document.getString("skills");
            String data = document.getString("data");
            Boolean flying = document.getBoolean("flying");
            if (flying == null) flying = (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR);
            users.add(new User(
                    UUID.fromString(document.getString("uuid")),
                    document.getString("name"),
                    gameMode,
                    SerializationUtil.deserializeLocation(document.getString("location")),
                    inventory == null ? null : (UserInventory) SerializationUtil.deserializeObject(inventory),
                    data == null ? null : (Map<String, UserData>) SerializationUtil.deserializeObject(data),
                    skills == null ? null : (EnumMap<SkillType, Double>) SerializationUtil.deserializeObject(skills),
                    flying,
                    flying
            ));
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
                new EnumMap<>(Map.of(
                        SkillType.COMBAT, 0.0,
                        SkillType.MINING, 0.0,
                        SkillType.FARMING, 0.0,
                        SkillType.FORAGING, 0.0
                )),
                player.isFlying(),
                player.getAllowFlight());
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