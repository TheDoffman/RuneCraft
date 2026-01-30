package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.plugin.PluginManager;

public final class AgilityBootstrap {
    public static void init(RuneCraft plugin) {
        AgilityCourseManager.init(plugin);
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new AgilityListener(), plugin);
    }
}