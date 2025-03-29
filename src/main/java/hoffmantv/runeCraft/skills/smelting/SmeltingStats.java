package hoffmantv.runeCraft.skills.smelting;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SmeltingStats {
    private int level;
    private double xp;

    public SmeltingStats() {
        this.level = 1;
        this.xp = 0;
    }

    /**
     * Loads the smelting stats for the given player from persistent storage.
     *
     * @param player The player whose smelting stats should be loaded.
     * @return A SmeltingStats object with the player's current XP and level.
     */
    public static SmeltingStats load(Player player) {
        UUID uuid = player.getUniqueId();
        SmeltingStats stats = new SmeltingStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "smelting");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "smelting");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    /**
     * Saves the player's smelting stats to persistent storage.
     *
     * @param player The player whose smelting stats should be saved.
     */
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "smelting", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "smelting", this.level);
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
     * Adjust the multiplier to balance leveling.
     *
     * @return The XP needed for the next smelting level.
     */
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 100;
    }

    /**
     * Adds experience to the smelting skill.
     * If enough XP is accumulated, levels up the skill (capped at level 99).
     * The stats are saved immediately after the update.
     *
     * @param xp     The amount of experience to add.
     * @param player The player whose smelting stats should be updated.
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
            // Optionally trigger a smelting level-up effect here.
        }
        save(player);
    }
}