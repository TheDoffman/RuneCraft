package hoffmantv.runeCraft.skills.mining;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MiningStats {
    private int level;
    private double xp;

    public MiningStats() {
        this.level = 1;
        this.xp = 0;
    }

    public static MiningStats load(Player player) {
        UUID uuid = player.getUniqueId();
        MiningStats stats = new MiningStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "mining");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "mining");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "mining", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "mining", this.level);
        PlayerSkillDataManager.saveData(RuneCraft.getInstance());
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    // XP required for next level (adjust multiplier as needed).
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 100;
    }

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
            SkillRewardUtils.triggerSkillRankUpEffect(player, "Mining", this.level);
            if (this.level % 5 == 0) {
                SkillRewardUtils.triggerSkillRankUpEffect(player, "Mining", this.level);
            }
        }
        save(player);
    }
}