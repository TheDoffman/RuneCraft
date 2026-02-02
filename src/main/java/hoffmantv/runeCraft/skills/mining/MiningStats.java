package hoffmantv.runeCraft.skills.mining;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class MiningStats extends BaseStats {
    public static MiningStats load(Player player) {
        MiningStats stats = new MiningStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "mining";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Mining";
    }
}