package hoffmantv.runeCraft.skills.woodcutting;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class WoodcuttingStats extends BaseStats {
    public static WoodcuttingStats load(Player player) {
        WoodcuttingStats stats = new WoodcuttingStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "woodcutting";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Woodcutting";
    }
}