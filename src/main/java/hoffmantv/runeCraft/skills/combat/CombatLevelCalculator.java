package hoffmantv.runeCraft.skills.combat;

import hoffmantv.runeCraft.skills.attack.AttackStats;
import hoffmantv.runeCraft.skills.attack.AttackStatsManager;
import hoffmantv.runeCraft.skills.defence.DefenceStats;
import hoffmantv.runeCraft.skills.defence.DefenceStatsManager;
import hoffmantv.runeCraft.skills.strength.StrengthStats;
import hoffmantv.runeCraft.skills.strength.StrengthStatsManager;
import org.bukkit.entity.Player;

public class CombatLevelCalculator {

    /**
     * Calculates the player's overall combat level as the sum of Attack, Defence, and Strength levels.
     *
     * @param player The player whose combat level is to be calculated.
     * @return The computed combat level.
     */
    public static int getCombatLevel(Player player) {
        int attackLevel = 0, defenceLevel = 0, strengthLevel = 0;

        AttackStats attackStats = AttackStatsManager.getStats(player);
        if (attackStats != null) {
            attackLevel = attackStats.getLevel();
        }

        DefenceStats defenceStats = DefenceStatsManager.getStats(player);
        if (defenceStats != null) {
            defenceLevel = defenceStats.getLevel();
        }

        StrengthStats strengthStats = StrengthStatsManager.getStats(player);
        if (strengthStats != null) {
            strengthLevel = strengthStats.getLevel();
        }

        // The combat level is the sum of Attack, Defence, and Strength levels.
        return attackLevel + defenceLevel + strengthLevel;
    }
}