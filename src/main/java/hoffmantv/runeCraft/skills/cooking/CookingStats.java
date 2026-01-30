package hoffmantv.runeCraft.skills.cooking;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class CookingStats extends BaseStats {
    private static final double XP_MULTIPLIER = 50;

    public static CookingStats load(Player player) {
        CookingStats stats = new CookingStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "cooking";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Cooking";
    }

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }
}