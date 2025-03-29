package hoffmantv.runeCraft.skills.smelting;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SmeltingStatsManager {
    private static final Map<UUID, SmeltingStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        SmeltingStats stats = SmeltingStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static SmeltingStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}