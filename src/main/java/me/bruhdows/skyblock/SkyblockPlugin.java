package me.bruhdows.skyblock;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import me.bruhdows.skyblock.command.ItemCommand;
import me.bruhdows.skyblock.gui.api.InventoryManager;
import me.bruhdows.skyblock.listener.JoinQuitListener;
import me.bruhdows.skyblock.manager.JedisManager;
import me.bruhdows.skyblock.manager.UserManager;
import me.bruhdows.skyblock.module.user.User;
import me.bruhdows.skyblock.storage.config.Configuration;
import me.bruhdows.skyblock.storage.config.Data;
import me.bruhdows.skyblock.storage.config.Messages;
import me.bruhdows.skyblock.handler.InvalidUsageHandler;
import me.bruhdows.skyblock.handler.MissingPermissionsHandler;
import me.bruhdows.skyblock.listener.ItemListener;
import me.bruhdows.skyblock.manager.ItemManager;
import me.bruhdows.skyblock.module.ability.impl.LeftClickAbility;
import me.bruhdows.skyblock.module.ability.impl.LeftShiftClickAbility;
import me.bruhdows.skyblock.module.item.ItemType;
import me.bruhdows.skyblock.module.item.Item;
import me.bruhdows.skyblock.module.item.Rarity;
import me.bruhdows.skyblock.module.item.StatType;
import me.bruhdows.skyblock.module.ability.impl.RightClickAbility;
import me.bruhdows.skyblock.storage.database.JedisAPI;
import me.bruhdows.skyblock.storage.database.JedisListener;
import me.bruhdows.skyblock.storage.database.MongoDB;
import me.bruhdows.skyblock.task.UserUpdateTask;
import me.bruhdows.skyblock.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public final class SkyblockPlugin extends JavaPlugin {

    @Getter
    private static SkyblockPlugin instance;

    private ItemManager itemManager;
    private UserManager userManager;
    private InventoryManager inventoryManager;
    private LiteCommands<CommandSender> liteCommands;

    private Configuration configuration;
    private Messages messages;
    private Data data;

    private JedisAPI jedisAPI;
    private MongoDB mongoDB;

    @Override
    public void onEnable() {
        instance = this;
        initConfigs();
        initDatabases();
        registerManagers();
        registerListeners();
        registerCommands();
        startTasks();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = this.userManager.getUser(player);
            if (user != null) {
                user.updateData(player);
                user.setLastServerId(data.getServerId());
                user.setUpdate(true);
            }
        });
        userManager.saveUsers();
        mongoDB.disconnect();
        jedisAPI.disconnect();
    }

    private void initConfigs() {
        configuration = ConfigManager.create(Configuration.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(getDataFolder(), "config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        messages = ConfigManager.create(Messages.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer());
            it.withBindFile(new File(getDataFolder(), "messages.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
        data = ConfigManager.create(Data.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer());
            it.withBindFile(new File(getDataFolder(), "data.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }

    private void initDatabases() {
        mongoDB = new MongoDB(configuration.getDatabase().getMongoDB());
        mongoDB.connect();

        jedisAPI = new JedisAPI(configuration.getDatabase().getRedis());
        jedisAPI.connect();

        JedisManager.subscribeChannels(this.jedisAPI, new JedisListener(this), JedisManager.getChannel());
    }

    private void registerManagers() {
        userManager = new UserManager(this);
        userManager.loadUsers();

        inventoryManager = new InventoryManager(this);

        itemManager = new ItemManager();
        itemManager.registerItem(
                new Item("ENRAGER",
                        ItemBuilder.of(new ItemStack(Material.IRON_AXE)).name("Enrager").get(),
                        ItemType.AXE,
                        new EnumMap<>(Map.of(
                                StatType.STRENGTH, 1,
                                StatType.DEFENSE, 2,
                                StatType.HEALTH, 5,
                                StatType.DAMAGE, 200)),
                        List.of(new LeftClickAbility(), new LeftShiftClickAbility(), new RightClickAbility()),
                        Rarity.EPIC));
    }

    private void registerListeners() {
        Set.of(
                new ItemListener(this),
                new JoinQuitListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        liteCommands = LiteCommandsBukkit.builder()
                .settings(settings -> settings.fallbackPrefix("skyblock").nativePermissions(true))
                .commands(
                    new ItemCommand()
                )
                .message(LiteBukkitMessages.PLAYER_ONLY, "&cOnly player can execute this command!")
                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> "&cPlayer &7" + input + " &cnot found!")
                .missingPermission(new MissingPermissionsHandler(this))
                .invalidUsage(new InvalidUsageHandler(this))
                .build();
    }

    private void startTasks() {
        new UserUpdateTask(this).runTaskTimer(this, 0L, 20L);
    }
}