package hoffmantv.runeCraft;

import hoffmantv.runeCraft.commands.*;
import hoffmantv.runeCraft.mobs.MobLevelListener;
import hoffmantv.runeCraft.mobs.MobSpawnManager;
import hoffmantv.runeCraft.mobs.MobSpawnRestrictionListener;
import hoffmantv.runeCraft.skills.combat.TurnBasedCombatListener;
import hoffmantv.runeCraft.skills.PlayerJoinListener;
import hoffmantv.runeCraft.skills.SkillManager;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.scoreboard.StatsLeaderboard;
import hoffmantv.runeCraft.skills.combat.CombatLevelNameUpdater;
import hoffmantv.runeCraft.skills.cooking.CookingListener;
import hoffmantv.runeCraft.skills.defence.ArmorEquipListener;
import hoffmantv.runeCraft.skills.firemaking.FiremakingListener;
import hoffmantv.runeCraft.skills.firemaking.LogPlacePreventionListener;
import hoffmantv.runeCraft.skills.fishing.FishingListener;
import hoffmantv.runeCraft.skills.fishing.FishingSpotsManager;
import hoffmantv.runeCraft.skills.mining.MiningBlockBreakPreventionListener;
import hoffmantv.runeCraft.skills.mining.MiningListener;
import hoffmantv.runeCraft.skills.mining.PickaxeHoldListener;
import hoffmantv.runeCraft.skills.smelting.SmeltingListener;
import hoffmantv.runeCraft.skills.strength.SwordHoldListener;
import hoffmantv.runeCraft.skills.woodcutting.AxeHoldListener;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class RuneCraft extends JavaPlugin {

    private static RuneCraft instance;
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
        StatsLeaderboard statsLeaderboard = new StatsLeaderboard();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                // Update the per-player scoreboard safely
                try {
                    statsLeaderboard.update(p);
                } catch (Throwable t) {
                    getLogger().warning("Failed updating scoreboard for " + p.getName() + ": " + t.getMessage());
                }
            }
        }, 0L, 100L);

        //Loads mobSpawns.yml
        MobSpawnManager.init(this);

        // Player Combat LVL
        CombatLevelNameUpdater.startUpdating();

        // Reloads all Skills.
        SkillManager.reloadAllSkills();
        getLogger().info("[RC] Skills Loaded.");

        getLogger().info("RuneCraft plugin enabled.");
    }

    @Override
    public void onDisable() {
        //Saves all YMLs
        initConfigs();

        getLogger().info("[RC] Plugin disabled.");
    }

    private void initConfigs() {
        saveDefaultConfig();
        PlayerSkillDataManager.saveData(this);
        FishingSpotsManager.init(this);
    }

    public static RuneCraft getInstance() {
        return instance;
    }
    private void initListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
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
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(executor);
        } else {
            getLogger().warning("Command '/" + name + "' is not defined in plugin.yml");
        }
    }
}