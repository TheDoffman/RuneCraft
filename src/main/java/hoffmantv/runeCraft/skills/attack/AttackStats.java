package hoffmantv.runeCraft.skills.attack;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class AttackStats extends BaseStats {
    public static AttackStats load(Player player) {
        AttackStats stats = new AttackStats();
        stats.loadFromPlayer(player);
        return stats;
    }

    @Override
    protected String getSkillKey() {
        return "attack";
    }

    @Override
    protected String getSkillDisplayName() {
        return "Attack";
    }
}