package hoffmantv.runeCraft.skills.cooking;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class CookingStatsManager extends BaseStatsManager<CookingStats> {
    private static final CookingStatsManager INSTANCE = new CookingStatsManager();

    @Override
    protected CookingStats load(Player player) {
        return CookingStats.load(player);
    }

    public static CookingStats getStats(Player player) {
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