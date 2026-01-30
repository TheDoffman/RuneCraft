package hoffmantv.runeCraft.skills.woodcutting;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class WoodcuttingStats extends BaseStats {
    private static final double XP_MULTIPLIER = 50;

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

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }
}