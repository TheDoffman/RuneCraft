package hoffmantv.runeCraft.skills.smelting;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class SmeltingStats extends BaseStats {
    private static final double XP_MULTIPLIER = 100;

    public static SmeltingStats load(Player player) {
        SmeltingStats stats = new SmeltingStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "smelting";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Smelting";
    }

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }
}