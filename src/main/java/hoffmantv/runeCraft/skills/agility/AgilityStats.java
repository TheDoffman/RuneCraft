package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class AgilityStats extends BaseStats {
    public static AgilityStats load(Player player) {
        AgilityStats stats = new AgilityStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "agility";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Agility";
    }

    @Override
    protected double getXpMultiplier() {
        return 0;
    }

    @Override
    public void addExperience(double amount, Player player) {
        xp += amount;
        while (level < 99 && xp >= AgilityXP.levelToXp(level + 1)) {
            level++;
            AgilityUI.levelUpToast(player, level);
        }
        save(player);
    }
}