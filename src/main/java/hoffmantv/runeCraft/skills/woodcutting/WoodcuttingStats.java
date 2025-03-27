package hoffmantv.runeCraft.skills.woodcutting;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WoodcuttingStats {
    private int level;
    private double xp;

    public WoodcuttingStats() {
        this.level = 1;
        this.xp = 0;
    }

    // Load woodcutting stats from persistent storage.
    public static WoodcuttingStats load(Player player) {
        UUID uuid = player.getUniqueId();
        WoodcuttingStats stats = new WoodcuttingStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "woodcutting");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "woodcutting");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    // Save woodcutting stats to persistent storage.
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "woodcutting", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "woodcutting", this.level);
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
        return Math.pow(level, 2) * 50; // Adjust the multiplier as needed.
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
            SkillRewardUtils.triggerSkillRankUpEffect(player, "WoodCutting", this.level);
            if (this.level % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "WoodCutting", this.level);
            }
        }
        save(player);
    }
}