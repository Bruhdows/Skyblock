package me.bruhdows.skyblock;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.utils.ParticleException;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import me.bruhdows.skyblock.api.gui.InventoryManager;
import me.bruhdows.skyblock.command.ItemCommand;
import me.bruhdows.skyblock.command.MobCommand;
import me.bruhdows.skyblock.handler.InvalidUsageHandler;
import me.bruhdows.skyblock.handler.MissingPermissionsHandler;
import me.bruhdows.skyblock.listener.DamageListener;
import me.bruhdows.skyblock.listener.ItemListener;
import me.bruhdows.skyblock.listener.JoinQuitListener;
import me.bruhdows.skyblock.manager.*;
import me.bruhdows.skyblock.module.ability.impl.LeftClickAbility;
import me.bruhdows.skyblock.module.ability.impl.LeftShiftClickAbility;
import me.bruhdows.skyblock.module.ability.impl.RightClickAbility;
import me.bruhdows.skyblock.module.item.impl.EnchantedDiamond;
import me.bruhdows.skyblock.module.item.impl.Enrager;
import me.bruhdows.skyblock.module.mob.impl.Dummy;
import me.bruhdows.skyblock.module.user.User;
import me.bruhdows.skyblock.storage.config.Configuration;
import me.bruhdows.skyblock.storage.config.Data;
import me.bruhdows.skyblock.storage.config.Messages;
import me.bruhdows.skyblock.storage.database.JedisAPI;
import me.bruhdows.skyblock.storage.database.JedisListener;
import me.bruhdows.skyblock.storage.database.MongoDB;
import me.bruhdows.skyblock.task.StatsTask;
import me.bruhdows.skyblock.task.UserUpdateTask;
import me.bruhdows.skyblock.util.TextUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
@Getter
public final class SkyblockPlugin extends JavaPlugin {

    @Getter
    private static SkyblockPlugin instance;

    private ItemManager itemManager;
    private MobManager mobManager;
    private AbilityManager abilityManager;
    private UserManager userManager;
    private InventoryManager inventoryManager;
    private LiteCommands<CommandSender> liteCommands;

    private Configuration configuration;
    private Messages messages;
    private Data data;

    private JedisAPI jedisAPI;
    private MongoDB mongoDB;

    private ParticleNativeAPI particleAPI;
    private MiniMessage miniMessage;

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
        jedisAPI.disconnect();
        mongoDB.disconnect();
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
        miniMessage = MiniMessage.miniMessage();

        try {
            particleAPI = ParticleNativeCore.loadAPI(this);
        } catch (ParticleException e) {
            TextUtil.severe("ParticleAPI not found.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        if (mongoDB != null || jedisAPI != null) {
            userManager = new UserManager(this);
            userManager.loadUsers();
        }

        itemManager = new ItemManager();
        itemManager.registerItem(new Enrager());
        itemManager.registerItem(new EnchantedDiamond());

        mobManager = new MobManager();
        mobManager.registerMob(new Dummy());

        abilityManager = new AbilityManager();
        abilityManager.registerAbility(new LeftClickAbility());
        abilityManager.registerAbility(new LeftShiftClickAbility());
        abilityManager.registerAbility(new RightClickAbility());
    }

    private void registerListeners() {
        Set.of(
                new ItemListener(this),
                new JoinQuitListener(this),
                new DamageListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        liteCommands = LiteCommandsBukkit.builder()
                .settings(settings -> settings.fallbackPrefix("skyblock").nativePermissions(true))
                .commands(
                    new ItemCommand(),
                    new MobCommand()
                )
                .message(LiteBukkitMessages.PLAYER_ONLY, "&cOnly player can execute this command!")
                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> "&cPlayer &7" + input + " &cnot found!")
                .missingPermission(new MissingPermissionsHandler())
                .invalidUsage(new InvalidUsageHandler())
                .build();
    }

    private void startTasks() {
        if (mongoDB != null || jedisAPI != null) {
            new UserUpdateTask(this).runTaskTimer(this, 0L, 20L);
        }
        new StatsTask(this).runTaskTimer(this, 0L, 20L);
    }
}
