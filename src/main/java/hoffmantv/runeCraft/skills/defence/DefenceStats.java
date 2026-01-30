package hoffmantv.runeCraft.skills.defence;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class DefenceStats extends BaseStats {
    private static final double XP_MULTIPLIER = 100;

    public static DefenceStats load(Player player) {
        DefenceStats stats = new DefenceStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "defence";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Defence";
    }

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }

    @Override
    protected void onLevelUp(Player player) {
        player.sendMessage("Defence level increased to " + level + "!");
    }

    @Override
    protected void onMilestoneLevel(Player player) {
        // Defence level-ups only message on each level.
    }
}