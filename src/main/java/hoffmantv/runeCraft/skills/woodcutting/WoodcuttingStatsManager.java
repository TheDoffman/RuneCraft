package hoffmantv.runeCraft.skills.woodcutting;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WoodcuttingStatsManager {
    private static final Map<UUID, WoodcuttingStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        WoodcuttingStats stats = WoodcuttingStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static WoodcuttingStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}