package hoffmantv.runeCraft.skills.strength;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class StrengthStats extends BaseStats {
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
}