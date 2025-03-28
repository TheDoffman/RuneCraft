package hoffmantv.runeCraft.skills.fishing;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FishingStats {
    private int level;
    private double xp;

    public FishingStats() {
        this.level = 1;
        this.xp = 0;
    }

    public static FishingStats load(Player player) {
        UUID uuid = player.getUniqueId();
        FishingStats stats = new FishingStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "fishing");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "fishing");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "fishing", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "fishing", this.level);
        PlayerSkillDataManager.saveData(RuneCraft.getInstance());
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    // XP required for next level. Adjust multiplier as needed.
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 75;
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
            // Optionally trigger a fishing level-up effect.
        }
        save(player);
    }
}