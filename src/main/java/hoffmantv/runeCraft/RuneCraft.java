package hoffmantv.runeCraft;

import hoffmantv.runeCraft.commands.*;
import hoffmantv.runeCraft.mobs.MobLevelListener;
import hoffmantv.runeCraft.mobs.MobLevelRewardsListener;
import hoffmantv.runeCraft.mobs.MobSpawnManager;
import hoffmantv.runeCraft.mobs.MobSpawnRestrictionListener;
import hoffmantv.runeCraft.skills.combat.TurnBasedCombatListener;
import hoffmantv.runeCraft.skills.PlayerJoinListener;
import hoffmantv.runeCraft.skills.PlayerQuitCleanupListener;
import hoffmantv.runeCraft.skills.SkillManager;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.scoreboard.StatsLeaderboard;
import hoffmantv.runeCraft.scoreboard.ScoreboardCleanupListener;
import hoffmantv.runeCraft.skills.combat.CombatLevelNameUpdater;
import hoffmantv.runeCraft.skills.combat.TurnBasedCombatSession;
import hoffmantv.runeCraft.skills.cooking.CookingListener;
import hoffmantv.runeCraft.skills.defence.ArmorEquipListener;
import hoffmantv.runeCraft.skills.firemaking.FiremakingListener;
import hoffmantv.runeCraft.skills.firemaking.FiremakingListenerHelper;
import hoffmantv.runeCraft.skills.firemaking.LogPlacePreventionListener;
import hoffmantv.runeCraft.skills.fishing.FishingListener;
import hoffmantv.runeCraft.skills.fishing.FishingSpotsManager;
import hoffmantv.runeCraft.skills.mining.MiningBlockBreakPreventionListener;
import hoffmantv.runeCraft.skills.mining.MiningListener;
import hoffmantv.runeCraft.skills.mining.MiningListenerHelper;
import hoffmantv.runeCraft.skills.mining.PickaxeHoldListener;
import hoffmantv.runeCraft.skills.smelting.SmeltingListener;
import hoffmantv.runeCraft.skills.strength.SwordHoldListener;
import hoffmantv.runeCraft.skills.woodcutting.AxeHoldListener;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingListener;
import hoffmantv.runeCraft.tasks.TaskRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class RuneCraft extends JavaPlugin {

    private static RuneCraft instance;
    private final Map<UUID, StatsLeaderboard> leaderboards = new HashMap<>();
    private final Set<UUID> scoreboardDisabled = new HashSet<>();
    private final TaskRegistry taskRegistry = new TaskRegistry();
    private BukkitTask scoreboardTask;
    private BukkitTask combatLevelTask;
    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(Objects.requireNonNull(instance, "RuneCraft instance not initialized"), key);
    }

    @Override
    public void onEnable() {
        instance = this;

        // Initialize the YAML file for storing player skill data.
        PlayerSkillDataManager.setup(this);
        PlayerSkillDataManager.reloadData();

        // Load default configuration
        initConfigs();

        // Load Listeners
        initListeners();

        // Load Commands
        initCommands();

        // Start periodic skills leaderboard updates (every 5 seconds).
        scoreboardTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                // Update the per-player scoreboard safely
                try {
                    if (scoreboardDisabled.contains(p.getUniqueId())) {
                        continue;
                    }
                    StatsLeaderboard leaderboard = leaderboards.computeIfAbsent(
                            p.getUniqueId(),
                            id -> new StatsLeaderboard()
                    );
                    leaderboard.update(p);
                } catch (Throwable t) {
                    getLogger().warning("Failed updating scoreboard for " + p.getName() + ": " + t.getMessage());
                }
            }
        }, 0L, 100L);

        //Loads mobSpawns.yml
        MobSpawnManager.init(this);

        // Player Combat LVL
        combatLevelTask = CombatLevelNameUpdater.startUpdating();

        // Reloads all Skills.
        SkillManager.reloadAllSkills();
        getLogger().info("[RC] Skills Loaded.");

        getLogger().info("RuneCraft plugin enabled.");
    }

    @Override
    public void onDisable() {
        if (scoreboardTask != null) {
            scoreboardTask.cancel();
            scoreboardTask = null;
        }
        if (combatLevelTask != null) {
            combatLevelTask.cancel();
            combatLevelTask = null;
        }

        FishingSpotsManager.shutdown();
        TurnBasedCombatSession.shutdownAll();
        PlayerSkillDataManager.saveData(this);
        leaderboards.clear();
        scoreboardDisabled.clear();
        taskRegistry.cancelAll();
        MiningListenerHelper.clearAll();
        FiremakingListenerHelper.clearAll();

        getLogger().info("[RC] Plugin disabled.");
    }

    private void initConfigs() {
        saveDefaultConfig();
        FishingSpotsManager.init(this);
    }

    public static RuneCraft getInstance() {
        return instance;
    }
    private void initListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerQuitCleanupListener(), this);
        pm.registerEvents(new ScoreboardCleanupListener(), this);
        pm.registerEvents(new WaterEntryPreventionListener(), this);
        pm.registerEvents(new WoodcuttingListener(), this);
        pm.registerEvents(new AxeHoldListener(), this);
        pm.registerEvents(new FiremakingListener(), this);
        pm.registerEvents(new LogPlacePreventionListener(), this);
        pm.registerEvents(new MiningListener(), this);
        pm.registerEvents(new MiningBlockBreakPreventionListener(), this);
        pm.registerEvents(new PickaxeHoldListener(), this);
        pm.registerEvents(new FishingListener(), this);
        pm.registerEvents(new CookingListener(), this);
        pm.registerEvents(new TurnBasedCombatListener(), this);
        pm.registerEvents(new MobSpawnRestrictionListener(), this);
        pm.registerEvents(new SmeltingListener(), this);
        pm.registerEvents(new MobLevelListener(), this);
        pm.registerEvents(new MobLevelRewardsListener(), this);
        pm.registerEvents(new SwordHoldListener(), this);
        pm.registerEvents(new ArmorEquipListener(), this);
    }
    private void initCommands(){
        registerCommand("setfishingspot", new SetFishingSpotCommand());
        registerCommand("clearinv", new ClearInventoryCommand());
        registerCommand("skills", new SkillsCommand());
        registerCommand("setmobspawn", new SetMobSpawnCommand());
        registerCommand("scoreboard", new ScoreboardToggleCommand());
        registerCommand("generatecourse", new GenerateComplexCourseCommand());
        registerCommand("agility", new AgilityCommand());
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(executor);
        } else {
            getLogger().warning("Command '/" + name + "' is not defined in plugin.yml");
        }
    }

    public void removeLeaderboard(UUID uuid) {
        leaderboards.remove(uuid);
        scoreboardDisabled.remove(uuid);
    }

    public void setScoreboardEnabled(UUID uuid, boolean enabled) {
        if (enabled) {
            scoreboardDisabled.remove(uuid);
        } else {
            scoreboardDisabled.add(uuid);
        }
    }

    public boolean isScoreboardEnabled(UUID uuid) {
        return !scoreboardDisabled.contains(uuid);
    }

    public TaskRegistry getTaskRegistry() {
        return taskRegistry;
    }
}