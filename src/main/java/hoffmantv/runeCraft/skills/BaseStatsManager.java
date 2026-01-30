package hoffmantv.runeCraft.skills;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseStatsManager<T extends BaseStats> {
    private final Map<UUID, T> cache = new ConcurrentHashMap<>();

    protected abstract T load(Player player);

    protected T getStatsInternal(Player player) {
        return cache.computeIfAbsent(player.getUniqueId(), id -> load(player));
    }

    protected void loadPlayerInternal(Player player) {
        cache.put(player.getUniqueId(), load(player));
    }

    protected void removePlayerInternal(Player player) {
        cache.remove(player.getUniqueId());
    }

    protected void clearInternal() {
        cache.clear();
    }
}
