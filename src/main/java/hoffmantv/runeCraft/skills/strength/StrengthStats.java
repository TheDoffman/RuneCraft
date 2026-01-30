package hoffmantv.runeCraft.skills.strength;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class StrengthStats extends BaseStats {
    private static final double XP_MULTIPLIER = 75;
    private static final double DAMAGE_XP_MULTIPLIER = 2.0;

    public static StrengthStats load(Player player) {
        StrengthStats stats = new StrengthStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "strength";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Strength";
    }

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }

    @Override
    protected double transformIncomingXp(double amount) {
        return amount * DAMAGE_XP_MULTIPLIER;
    }
}