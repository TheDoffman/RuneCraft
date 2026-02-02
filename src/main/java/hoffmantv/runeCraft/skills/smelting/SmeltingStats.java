package hoffmantv.runeCraft.skills.smelting;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class SmeltingStats extends BaseStats {
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
}