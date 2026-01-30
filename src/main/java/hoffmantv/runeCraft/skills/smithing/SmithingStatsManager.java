package hoffmantv.runeCraft.skills.smithing;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class SmithingStatsManager extends BaseStatsManager<SmithingStats> {
    private static final SmithingStatsManager INSTANCE = new SmithingStatsManager();

    @Override
    protected SmithingStats load(Player player) {
        return SmithingStats.load(player);
    }

    public static SmithingStats getStats(Player player) {
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
