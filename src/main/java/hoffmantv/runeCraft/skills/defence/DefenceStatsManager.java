package hoffmantv.runeCraft.skills.defence;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class DefenceStatsManager extends BaseStatsManager<DefenceStats> {
    private static final DefenceStatsManager INSTANCE = new DefenceStatsManager();

    @Override
    protected DefenceStats load(Player player) {
        return DefenceStats.load(player);
    }

    public static DefenceStats getStats(Player player) {
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