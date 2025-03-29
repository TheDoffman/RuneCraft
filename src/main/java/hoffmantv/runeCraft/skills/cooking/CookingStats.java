package hoffmantv.runeCraft.skills.cooking;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CookingStats {
    private int level;
    private double xp;

    public CookingStats() {
        this.level = 1;
        this.xp = 0;
    }

    /**
     * Loads the cooking stats for the given player from persistent storage.
     */
    public static CookingStats load(Player player) {
        UUID uuid = player.getUniqueId();
        CookingStats stats = new CookingStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "cooking");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "cooking");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    /**
     * Saves the player's cooking stats to persistent storage.
     */
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "cooking", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "cooking", this.level);
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
     * Adjust the multiplier as needed for your desired leveling curve.
     */
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 50;
    }

    /**
     * Adds experience to the cooking skill.
     * If enough XP is accumulated, levels up the skill (capped at level 99).
     * The stats are saved immediately after the update.
     *
     * @param xp     The amount of experience to add.
     * @param player The player whose cooking stats should be updated.
     */
    public void addExperience(double xp, Player player) {
        if (level >= 99) {
            this.xp += xp;
            save(player);
            return;
        }
        this.xp += xp;
        while (this.xp >= xpForNextLevel() && level < 99) {
            this.xp -= xpForNextLevel();
            level++;
            // Trigger individual level-up effect for Cooking.
            SkillRewardUtils.triggerSkillRankUpEffect(player, "Cooking", this.level);
            if (this.level % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "Cooking", this.level);
            }
        }
        save(player);
    }
}