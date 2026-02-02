package hoffmantv.runeCraft.skills.smithing;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class SmithingStats extends BaseStats {
    public static SmithingStats load(Player player) {
        SmithingStats stats = new SmithingStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "smithing";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Smithing";
    }
}