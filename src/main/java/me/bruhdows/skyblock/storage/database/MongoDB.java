package me.bruhdows.skyblock.storage.database;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.storage.config.section.MongoDBSection;
import me.bruhdows.skyblock.util.TextUtil;
import org.bson.Document;
import org.bukkit.Bukkit;

@Getter
public class MongoDB {

    private MongoClient client;
    private MongoDatabase database;
    private final MongoDBSection section;

    public MongoDB(MongoDBSection section) {
        this.section = section;
    }

    public void connect() {
        String connectionString;

        if (!section.getUrl().isEmpty()) connectionString = section.getUrl();
        else connectionString = "mongodb://" +
                section.getUsername() + ":" +
                section.getPassword() + "@" +
                section.getAddress() + "/?retryWrites=true&w=majority";

        try {
            client = MongoClients.create(connectionString);
            database = client.getDatabase(section.getDatabase());
            database.runCommand(new Document("ping", 1));
            TextUtil.info("&a[MongoDB]", "Successfully connected.");
        } catch (MongoException e) {
            TextUtil.severe("&c[MongoDB]", "An error occurred while connecting.");
            TextUtil.severe("&c[MongoDB]", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(SkyblockPlugin.getInstance());
        }
    }

    public void disconnect() {
        if (client != null) client.close();
    }

    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }
}
