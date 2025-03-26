// File: PlayerCombatStatsManager.java
package hoffmantv.runeCraft.skilling;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class PlayerCombatStatsManager {
    private static final HashMap<UUID, CombatStats> statsMap = new HashMap<>();

    // Retrieves a player's combat stats, creating a new instance if needed.
    public static CombatStats getStats(Player player) {
        return statsMap.computeIfAbsent(player.getUniqueId(), k -> new CombatStats());
    }

    // Optionally remove stats when a player logs out or for cleanup.
    public static void removeStats(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}

/*
Integration Suggestions:
- Use these classes to manage and display player skilling stats related to combat.
- For example, award XP in CombatStats when a mob is defeated.
- Create a command (e.g., /combatstats) to allow players to view their current combat stats.
- Consider persisting these stats using a file or database for long-term progression.
*/