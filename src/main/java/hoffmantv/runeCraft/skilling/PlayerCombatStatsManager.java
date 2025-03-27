// Updated PlayerCombatStatsManager.java in hoffmantv.runeCraft.skilling
package hoffmantv.runeCraft.skilling;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCombatStatsManager {
    private static final Map<UUID, CombatStats> statsMap = new HashMap<>();

    /**
     * Loads the player's combat stats from persistent storage and caches them.
     * Should be called when a player joins.
     *
     * @param player The player to load stats for.
     */
    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        CombatStats stats = CombatStats.load(player);
        statsMap.put(uuid, stats);
    }

    /**
     * Retrieves the cached CombatStats for the player.
     *
     * @param player The player whose stats are requested.
     * @return The player's CombatStats, or null if not loaded.
     */
    public static CombatStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    /**
     * Removes the player's combat stats from the cache, e.g., when they leave.
     *
     * @param player The player whose stats should be removed.
     */
    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}