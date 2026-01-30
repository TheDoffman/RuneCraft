// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityStats.java
package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.skills.PlayerSkillDataManager;
import org.bukkit.entity.Player;

public class AgilityStats {
    private static final String KEY_XP = "skills.agility.xp";
    private static final String KEY_LVL = "skills.agility.level";

    private double xp;
    private int level;

    public AgilityStats(double xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public double getXp() { return xp; }
    public int getLevel() { return level; }

    public void addExperience(double amount, Player player) {
        xp += amount;
        while (level < 99 && xp >= AgilityXP.levelToXp(level + 1)) {
            level++;
            AgilityUI.levelUpToast(player, level);
        }
        save(player);
    }

    public void save(Player player) {
        PlayerSkillDataManager.setPlayerSkillXP(player.getUniqueId(), "Agility", xp);
        PlayerSkillDataManager.setPlayerSkillLevel(player.getUniqueId(), "Agility", level);
        PlayerSkillDataManager.saveNow();
    }

    public static AgilityStats load(Player player) {
        double x = PlayerSkillDataManager.getPlayerSkillXP(player.getUniqueId(), "Agility");
        int lvl = PlayerSkillDataManager.getPlayerSkillLevel(player.getUniqueId(), "Agility");
        if (lvl <= 0) lvl = 1;
        return new AgilityStats(x, lvl);
    }
}