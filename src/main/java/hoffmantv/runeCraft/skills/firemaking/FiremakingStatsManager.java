package hoffmantv.runeCraft.skills.firemaking;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class FiremakingStatsManager extends BaseStatsManager<FiremakingStats> {
    private static final FiremakingStatsManager INSTANCE = new FiremakingStatsManager();

    @Override
    protected FiremakingStats load(Player player) {
        return FiremakingStats.load(player);
    }

    public static FiremakingStats getStats(Player player) {
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