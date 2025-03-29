package hoffmantv.runeCraft.skills.cooking;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CookingStatsManager {
    private static final Map<UUID, CookingStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        CookingStats stats = CookingStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static CookingStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}