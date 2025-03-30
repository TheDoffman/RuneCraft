package hoffmantv.runeCraft.skills.defence;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DefenceStats {
    private int level;
    private double xp;

    public DefenceStats() {
        this.level = 1;
        this.xp = 0;
    }

    /**
     * Loads a player's Defence stats from persistent storage.
     */
    public static DefenceStats load(Player player) {
        UUID uuid = player.getUniqueId();
        DefenceStats stats = new DefenceStats();
        stats.xp = PlayerSkillDataManager.getPlayerSkillXP(uuid.toString(), "defence");
        stats.level = PlayerSkillDataManager.getPlayerSkillRank(uuid.toString(), "defence");
        if (stats.level < 1) {
            stats.level = 1;
        }
        return stats;
    }

    /**
     * Saves the player's Defence stats to persistent storage.
     */
    public void save(Player player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        PlayerSkillDataManager.setPlayerSkillXP(id, "defence", this.xp);
        PlayerSkillDataManager.setPlayerSkillRank(id, "defence", this.level);
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
     */
    private double xpForNextLevel() {
        return Math.pow(level, 2) * 100;
    }

    /**
     * Adds experience to the Defence skill.
     *
     * @param xp     The amount of XP to add.
     * @param player The player whose Defence stats should be updated.
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
            player.sendMessage("Defence level increased to " + level + "!");
        }
        save(player);
    }
}