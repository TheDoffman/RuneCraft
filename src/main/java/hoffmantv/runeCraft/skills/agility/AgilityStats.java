package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class AgilityStats extends BaseStats {
    public static AgilityStats load(Player player) {
        AgilityStats stats = new AgilityStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "agility";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Agility";
    }

    @Override
    protected void onLevelUp(Player player) {
        AgilityUI.levelUpToast(player, level);
    }

    @Override
    protected void onMilestoneLevel(Player player) {
        // Agility uses a custom toast per level.
    }
}