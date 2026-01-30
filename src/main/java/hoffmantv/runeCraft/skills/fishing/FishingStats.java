package hoffmantv.runeCraft.skills.fishing;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class FishingStats extends BaseStats {
    private static final double XP_MULTIPLIER = 75;

    public static FishingStats load(Player player) {
        FishingStats stats = new FishingStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "fishing";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Fishing";
    }

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }
}