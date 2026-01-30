package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public final class AgilityStatsManager extends BaseStatsManager<AgilityStats> {
    private static final AgilityStatsManager INSTANCE = new AgilityStatsManager();

    @Override
    protected AgilityStats load(Player player) {
        return AgilityStats.load(player);
    }

    public static AgilityStats get(Player player) {
        return INSTANCE.getStatsInternal(player);
    }

    public static AgilityStats getStats(Player player) {
        return INSTANCE.getStatsInternal(player);
    }

    public static void loadPlayer(Player player) {
        INSTANCE.loadPlayerInternal(player);
    }

    public static void removePlayer(Player player) {
        INSTANCE.removePlayerInternal(player);
    }

    public static void clear() {
        INSTANCE.clearInternal();
    }
}