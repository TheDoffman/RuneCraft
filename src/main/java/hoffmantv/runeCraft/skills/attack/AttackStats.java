package hoffmantv.runeCraft.skills.attack;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AttackStats {
    private int level;
    private double xp;

    public AttackStats() {
        this.level = 1;
        this.xp = 0;
    }

    /**
     * Loads a player's AttackStats from persistent storage.
     * If no data is found, defaults to level 1 and 0 xp.
     */
    public static AttackStats load(Player player) {
        UUID uuid = player.getUniqueId();
        AttackStats stats = new AttackStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "attack");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "attack");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    /**
     * Saves the player's AttackStats to persistent storage.
     */
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "attack", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "attack", this.level);
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
     * Adjust the multiplier as needed for your leveling curve.
     * Here, we use a quadratic formula: level^2 * 100.
     */
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 100;
    }

    /**
     * Adds experience based on the damage inflicted.
     * For example, with a multiplier of 1.5, each point of damage gives 1.5 XP.
     *
     * @param damage The amount of damage dealt in the hit.
     * @param player The player receiving the XP.
     */
    public void addExperience(double damage, Player player) {
        double xpReward = damage * 1.5;  // Adjust the multiplier as needed.
        if (level >= 99) {
            this.xp += xpReward;
            save(player);
            return;
        }
        this.xp += xpReward;
        while (this.xp >= xpForNextLevel() && level < 99) {
            this.xp -= xpForNextLevel();
            level++;
            player.sendMessage("Attack level increased to " + level + "!");
        }
        save(player);
    }
}