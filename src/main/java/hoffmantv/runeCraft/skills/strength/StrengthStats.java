package hoffmantv.runeCraft.skills.strength;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StrengthStats {
    private int level;
    private double xp;

    public StrengthStats() {
        this.level = 1;
        this.xp = 0;
    }

    /**
     * Loads a player's StrengthStats from persistent storage.
     * If no data is found, defaults to level 1 and 0 XP.
     */
    public static StrengthStats load(Player player) {
        UUID uuid = player.getUniqueId();
        StrengthStats stats = new StrengthStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "strength");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "strength");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    /**
     * Saves the player's StrengthStats to persistent storage.
     */
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "strength", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "strength", this.level);
        PlayerSkillDataManager.saveData(RuneCraft.getInstance());
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    /**
     * Calculates the XP required for the next level.
     * For example, we use a quadratic formula: level^2 * 75.
     */
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 75;
    }

    /**
     * Adds XP to the Strength skill based on the damage dealt.
     * For example, here each point of damage gives 2 XP.
     *
     * @param damage The amount of damage (or strength action value).
     * @param player The player whose strength stats should be updated.
     */
    public void addExperience(double damage, Player player) {
        double xpReward = damage * 2.0;  // Adjust the multiplier as needed.
        if (level >= 99) {
            this.xp += xpReward;
            save(player);
            return;
        }
        this.xp += xpReward;
        while (this.xp >= xpForNextLevel() && level < 99) {
            this.xp -= xpForNextLevel();
            level++;
            // Trigger individual level-up effect for Strength.
            SkillRewardUtils.triggerSkillRankUpEffect(player, "Strength", this.level);
            if (this.level % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "Strength", this.level);
            }
        }
        save(player);
    }
}