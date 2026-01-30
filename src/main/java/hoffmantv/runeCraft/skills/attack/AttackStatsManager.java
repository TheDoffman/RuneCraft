package hoffmantv.runeCraft.skills.attack;

import hoffmantv.runeCraft.skills.BaseStatsManager;
import org.bukkit.entity.Player;

public class AttackStatsManager extends BaseStatsManager<AttackStats> {
    private static final AttackStatsManager INSTANCE = new AttackStatsManager();

    @Override
    protected AttackStats load(Player player) {
        return AttackStats.load(player);
    }

    public static AttackStats getStats(Player player) {
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