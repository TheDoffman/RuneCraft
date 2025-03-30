package hoffmantv.runeCraft.skills.strength;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrengthStatsManager {
    private static final Map<UUID, StrengthStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        StrengthStats stats = StrengthStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static StrengthStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}