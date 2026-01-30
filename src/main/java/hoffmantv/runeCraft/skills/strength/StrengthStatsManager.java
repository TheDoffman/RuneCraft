package hoffmantv.runeCraft.skills.strength;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class StrengthStatsManager extends BaseStatsManager<StrengthStats> {
    private static final StrengthStatsManager INSTANCE = new StrengthStatsManager();

    @Override
    protected StrengthStats load(Player player) {
        return StrengthStats.load(player);
    }

    public static StrengthStats getStats(Player player) {
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