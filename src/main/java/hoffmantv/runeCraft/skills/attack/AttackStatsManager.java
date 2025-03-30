package hoffmantv.runeCraft.skills.attack;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttackStatsManager {
    private static final Map<UUID, AttackStats> statsMap = new HashMap<>();

    public static void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        AttackStats stats = AttackStats.load(player);
        statsMap.put(uuid, stats);
    }

    public static AttackStats getStats(Player player) {
        return statsMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        statsMap.remove(player.getUniqueId());
    }
}