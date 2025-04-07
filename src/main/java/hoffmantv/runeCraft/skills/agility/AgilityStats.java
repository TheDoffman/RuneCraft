package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AgilityStats {
    private int level;
    private double xp;

    public AgilityStats() {
        this.level = 1;
        this.xp = 0;
    }

    /**
     * Loads a player's Agility stats from persistent storage.
     * Defaults to level 1 and 0 XP if no data is found.
     *
     * @param player The player whose Agility stats to load.
     * @return The AgilityStats for the player.
     */
    public static AgilityStats load(Player player) {
        UUID uuid = player.getUniqueId();
        AgilityStats stats = new AgilityStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "agility");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "agility");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    /**
     * Saves the player's Agility stats to persistent storage.
     *
     * @param player The player whose Agility stats to save.
     */
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "agility", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "agility", this.level);
        PlayerSkillDataManager.saveData(RuneCraft.getInstance());
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    /**
     * Calculates the XP required for the next Agility level.
     * Adjust the multiplier to balance progression.
     *
     * @return XP needed for the next level.
     */
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 100; // Example: quadratic curve.
    }

    /**
     * Adds XP to the Agility skill.
     * If the XP exceeds the threshold, the level increases (up to 99).
     *
     * @param xpReward The XP to add.
     * @param player   The player receiving the XP.
     */
    public void addExperience(double xpReward, Player player) {
        if (level >= 99) {
            this.xp += xpReward;
            save(player);
            return;
        }
        this.xp += xpReward;
        while (this.xp >= xpForNextLevel() && level < 99) {
            this.xp -= xpForNextLevel();
            level++;
            // Trigger individual level-up effect for Cooking.
            SkillRewardUtils.triggerSkillRankUpEffect(player, "Agility", this.level);
            if (this.level % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "Agility", this.level);
            }
        }
        save(player);
    }
}