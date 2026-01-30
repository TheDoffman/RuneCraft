package hoffmantv.runeCraft.skills.smithing;

import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import org.bukkit.entity.Player;

public class SmithingStats {

    private double xp;
    private int level;

    public SmithingStats(double xp) {
        this.xp = xp;
        this.level = calculateLevel(xp);
    }

    public void addExperience(double amount, Player player) {
        this.xp += amount;
        int oldLevel = this.level;
        this.level = calculateLevel(this.xp);

        if (this.level > oldLevel) {
            // Trigger level-up announcement if needed
            hoffmantv.runeCraft.skills.SkillRewardUtils.triggerSkillRankUpEffect(player, "Smithing", this.level);
        }
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
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

    public void save(Player player) {
        PlayerSkillDataManager.setPlayerSkillXP(String.valueOf(player.getUniqueId()), "Smithing", this.xp);
    }
}