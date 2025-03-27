package hoffmantv.runeCraft.skills.mining;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningStatsManager {
    private static final Map<UUID, MiningStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        MiningStats stats = MiningStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static MiningStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}