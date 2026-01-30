package hoffmantv.runeCraft.skills.firemaking;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class FiremakingStats extends BaseStats {
    private static final double XP_MULTIPLIER = 100;

    public static FiremakingStats load(Player player) {
        FiremakingStats stats = new FiremakingStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "firemaking";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Firemaking";
    }

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }
}