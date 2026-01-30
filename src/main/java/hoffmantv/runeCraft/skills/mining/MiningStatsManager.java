package hoffmantv.runeCraft.skills.mining;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class MiningStatsManager extends BaseStatsManager<MiningStats> {
    private static final MiningStatsManager INSTANCE = new MiningStatsManager();

    @Override
    protected MiningStats load(Player player) {
        return MiningStats.load(player);
    }

    public static MiningStats getStats(Player player) {
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