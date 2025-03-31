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
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class RuneCraft extends JavaPlugin {

    private static RuneCraft instance;
    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(instance, key);
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

        //Loads mobSpawns.yml
        MobSpawnManager.init(this);

        // Player Combat LVL
        CombatLevelNameUpdater.startUpdating();

        // Reloads all Skills.
        SkillManager.reloadAllSkills();
        getLogger().info("[RC] Skills Loaded.");

        // Initialize and schedule the leaderboard update.
        StatsLeaderboard statsLeaderboard = new StatsLeaderboard();

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
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new WoodcuttingListener(), this);
        getServer().getPluginManager().registerEvents(new AxeHoldListener(), this);
        getServer().getPluginManager().registerEvents(new FiremakingListener(), this);
        getServer().getPluginManager().registerEvents(new LogPlacePreventionListener(), this);
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MiningBlockBreakPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new PickaxeHoldListener(), this);
        getServer().getPluginManager().registerEvents(new FishingListener(), this);
        getServer().getPluginManager().registerEvents(new CookingListener(), this);
        getServer().getPluginManager().registerEvents(new TurnBasedCombatListener(), this);
        getServer().getPluginManager().registerEvents(new MobSpawnRestrictionListener(), this);
        getServer().getPluginManager().registerEvents(new SmeltingListener(), this);
        getServer().getPluginManager().registerEvents(new MobLevelListener(), this);
        getServer().getPluginManager().registerEvents(new SwordHoldListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(), this);
    }
    private void initCommands(){
        getCommand("setfishingspot").setExecutor(new SetFishingSpotCommand());
        getCommand("clearinv").setExecutor(new ClearInventoryCommand());
        getCommand("skills").setExecutor(new SkillsCommand());
        getCommand("setmobspawn").setExecutor(new SetMobSpawnCommand());
        getCommand("scoreboard").setExecutor(new ScoreboardToggleCommand());
    }
}