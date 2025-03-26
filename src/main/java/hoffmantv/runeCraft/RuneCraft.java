// RuneCraft.java
package hoffmantv.runeCraft;

import hoffmantv.runeCraft.skilling.PlayerSkillDataManager;
import org.bukkit.plugin.java.JavaPlugin;
import hoffmantv.runeCraft.combat.CombatCommand;
import hoffmantv.runeCraft.combat.CombatListener;
import hoffmantv.runeCraft.combat.CombatMovementListener;

public final class RuneCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        PlayerSkillDataManager.setup(this);
        initConfig();
        initModules();
        getLogger().info("RuneCraft plugin enabled. OSRS features are initializing...");
    }

    @Override
    public void onDisable() {
        getLogger().info("RuneCraft plugin disabled.");
    }

    private void initConfig() {
        saveDefaultConfig();
        getLogger().info("Configuration loaded.");
    }

    private void initModules() {
        // Register combat commands and event listeners
        if (getCommand("attack") != null) {
            getCommand("attack").setExecutor(new CombatCommand());
        }
        // Pass the plugin instance to CombatListener so it can provide it to the turn-based combat session.
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        // Register movement listener to prevent players from moving during turn-based combat
        getServer().getPluginManager().registerEvents(new CombatMovementListener(), this);
        getLogger().info("Combat module initialized.");
    }
}