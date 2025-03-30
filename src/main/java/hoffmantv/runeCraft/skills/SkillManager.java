package hoffmantv.runeCraft.skills;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStatsManager;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
import hoffmantv.runeCraft.skills.mining.MiningStatsManager;
import hoffmantv.runeCraft.skills.fishing.FishingStatsManager;
import hoffmantv.runeCraft.skills.cooking.CookingStatsManager;
import hoffmantv.runeCraft.skills.smelting.SmeltingStatsManager;

public class SkillManager {

    public static void reloadAllSkills() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Reload Woodcutting Stats
            WoodcuttingStatsManager.loadPlayer(player);
            // Reload Firemaking Stats
            FiremakingStatsManager.loadPlayer(player);
            // Reload Mining Stats
            MiningStatsManager.loadPlayer(player);
            // Reload Fishing Stats
            FishingStatsManager.loadPlayer(player);
            // Reload Cooking Stats
            CookingStatsManager.loadPlayer(player);
            // Reload Smelting Stats
            SmeltingStatsManager.loadPlayer(player);
        }
    }
}