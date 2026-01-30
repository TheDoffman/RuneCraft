// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityStatsManager.java
package hoffmantv.runeCraft.skills.agility;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class AgilityStatsManager {
    private static final Map<UUID, AgilityStats> CACHE = new ConcurrentHashMap<>();

    public static AgilityStats get(Player p) {
        return CACHE.computeIfAbsent(p.getUniqueId(), id -> AgilityStats.load(p));
    }

    public static void save(Player p) {
        AgilityStats s = CACHE.get(p.getUniqueId());
        if (s != null) s.save(p);
    }

    public static void unload(Player p) {
        save(p);
        CACHE.remove(p.getUniqueId());
    }
}