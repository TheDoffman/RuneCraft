package hoffmantv.runeCraft.skills.defence;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefenceStatsManager {
    private static final Map<UUID, DefenceStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        DefenceStats stats = DefenceStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static DefenceStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}