package hoffmantv.runeCraft.skills.agility;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AgilityStatsManager {
    private static final Map<UUID, AgilityStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        AgilityStats stats = AgilityStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static AgilityStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}