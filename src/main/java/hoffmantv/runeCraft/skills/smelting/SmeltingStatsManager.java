package hoffmantv.runeCraft.skills.smelting;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class SmeltingStatsManager extends BaseStatsManager<SmeltingStats> {
    private static final SmeltingStatsManager INSTANCE = new SmeltingStatsManager();

    @Override
    protected SmeltingStats load(Player player) {
        return SmeltingStats.load(player);
    }

    public static SmeltingStats getStats(Player player) {
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