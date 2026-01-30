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

    @Override
    protected double getXpMultiplier() {
        return 0;
    }

    @Override
    public void loadFromPlayer(Player player) {
        super.loadFromPlayer(player);
        this.level = calculateLevel(this.xp);
    }

    @Override
    public void addExperience(double amount, Player player) {
        this.xp += amount;
        int oldLevel = this.level;
        this.level = calculateLevel(this.xp);
        if (this.level > oldLevel) {
            for (int lvl = oldLevel + 1; lvl <= this.level; lvl++) {
                this.level = lvl;
                onLevelUp(player);
                if (this.level % 5 == 0) {
                    onMilestoneLevel(player);
                }
            }
        }
        save(player);
    }

    private int calculateLevel(double xp) {
        int level = 1;
        double requiredXP = 83;

        while (xp >= requiredXP && level < 99) {
            level++;
            requiredXP += Math.floor(level + 300.0 * Math.pow(2.0, level / 7.0));
        }
        return level;
    }
}