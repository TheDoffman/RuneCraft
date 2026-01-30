package hoffmantv.runeCraft.skills.fishing;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class FishingStatsManager extends BaseStatsManager<FishingStats> {
    private static final FishingStatsManager INSTANCE = new FishingStatsManager();

    @Override
    protected FishingStats load(Player player) {
        return FishingStats.load(player);
    }

    public static FishingStats getStats(Player player) {
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