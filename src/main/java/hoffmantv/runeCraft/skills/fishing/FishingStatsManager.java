package hoffmantv.runeCraft.skills.fishing;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FishingStatsManager {
    private static final Map<UUID, FishingStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        FishingStats stats = FishingStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static FishingStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}