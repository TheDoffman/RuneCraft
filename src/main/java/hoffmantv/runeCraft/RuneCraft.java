package hoffmantv.runeCraft;

import hoffmantv.runeCraft.combat.*;
import hoffmantv.runeCraft.mobs.MobDeathListener;
import hoffmantv.runeCraft.skills.PlayerCombatStatsManager;
import hoffmantv.runeCraft.mobs.MobSpawnListener;
import hoffmantv.runeCraft.commands.TestLevelUpCommand;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.scoreboard.StatsLeaderboard;
import hoffmantv.runeCraft.skills.firemaking.FiremakingListener;
import hoffmantv.runeCraft.skills.woodcutting.AxeHoldListener;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class RuneCraft extends JavaPlugin {

    private static RuneCraft instance;
    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(instance, key);
    }
    private StatsLeaderboard statsLeaderboard;

    @Override
    public void onEnable() {
        instance = this;
        // Load default configuration
        initConfig();
        // Initialize modules including turn-based combat movement restriction
        initModules();
        // Initialize the YAML file for storing player skill data.
        PlayerSkillDataManager.setup(this);
        PlayerSkillDataManager.reloadData();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerCombatStatsManager.loadPlayer(player);
        }
        // Initialize and schedule the leaderboard update.
        statsLeaderboard = new StatsLeaderboard();
        Bukkit.getScheduler().runTaskTimer(this, statsLeaderboard::update, 0L, 100L);
        // Register command executors and event listeners.
        getServer().getPluginManager().registerEvents(new CombatChatListener(), this);
        getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
        getServer().getPluginManager().registerEvents(new SwordHoldListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorRightClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new WoodcuttingListener(), this);
        getServer().getPluginManager().registerEvents(new AxeHoldListener(), this);
        getServer().getPluginManager().registerEvents(new FiremakingListener(), this);


        // Register the test level up command.
        if (getCommand("testlevelup") != null) {
            getCommand("testlevelup").setExecutor(new TestLevelUpCommand());
        }
        getLogger().info("RuneCraft plugin enabled.");
    }

    @Override
    public void onDisable() {
        // Save player skill data on disable
        hoffmantv.runeCraft.skills.PlayerSkillDataManager.saveData(this);
        getLogger().info("RuneCraft plugin disabled.");
    }

    private void initConfig() {
        saveDefaultConfig();
        getLogger().info("Configuration loaded.");
    }

    private void initModules() {
        // Register combat commands and event listeners
        if (getCommand("attack") != null) {
            getCommand("attack").setExecutor(new hoffmantv.runeCraft.combat.CombatCommand());
        }
        getServer().getPluginManager().registerEvents(new hoffmantv.runeCraft.combat.CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new hoffmantv.runeCraft.combat.CombatMovementListener(), this);
        getLogger().info("Combat module initialized.");
    }

    public static RuneCraft getInstance() {
        return instance;
    }
}