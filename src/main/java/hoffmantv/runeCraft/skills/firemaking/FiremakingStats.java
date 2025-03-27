package hoffmantv.runeCraft.skills.firemaking;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FiremakingStats {
    private int level;
    private double xp;

    public FiremakingStats() {
        this.level = 1;
        this.xp = 0;
    }

    // Loads firemaking stats from persistent storage.
    public static FiremakingStats load(Player player) {
        UUID uuid = player.getUniqueId();
        FiremakingStats stats = new FiremakingStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "firemaking");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "firemaking");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    // Saves firemaking stats to persistent storage.
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "firemaking", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "firemaking", this.level);
        // Immediately flush changes to file.
        PlayerSkillDataManager.saveData(RuneCraft.getInstance());
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    // Calculate the XP required for the next level using a quadratic formula.
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 100; // Adjust multiplier as needed.
    }

    // Add XP and immediately save the stats. Level up if the XP threshold is reached.
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
            // Trigger individual level-up effect for Strength.
            SkillRewardUtils.triggerSkillRankUpEffect(player, "FireMaking", this.level);
            if (this.level % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "FireMaking", this.level);
            }
        }
        save(player);
    }
}