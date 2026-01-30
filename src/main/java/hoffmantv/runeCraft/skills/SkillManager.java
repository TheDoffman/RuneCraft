package hoffmantv.runeCraft.skills;

import hoffmantv.runeCraft.skills.agility.AgilityStatsManager;
import hoffmantv.runeCraft.skills.attack.AttackStatsManager;
import hoffmantv.runeCraft.skills.defence.DefenceStatsManager;
import hoffmantv.runeCraft.skills.strength.StrengthStatsManager;
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
            WoodcuttingStatsManager.loadPlayer(player);
            FiremakingStatsManager.loadPlayer(player);
            MiningStatsManager.loadPlayer(player);
            FishingStatsManager.loadPlayer(player);
            CookingStatsManager.loadPlayer(player);
            SmeltingStatsManager.loadPlayer(player);
            AttackStatsManager.loadPlayer(player);
            StrengthStatsManager.loadPlayer(player);
            DefenceStatsManager.loadPlayer(player);
            AgilityStatsManager.unload(player);
        }
    }
}