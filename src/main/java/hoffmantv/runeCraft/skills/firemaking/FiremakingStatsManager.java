package hoffmantv.runeCraft.skills.firemaking;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FiremakingStatsManager {
    private static final Map<UUID, FiremakingStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        FiremakingStats stats = FiremakingStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static FiremakingStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}