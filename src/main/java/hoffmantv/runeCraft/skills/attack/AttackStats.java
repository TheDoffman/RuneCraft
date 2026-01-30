package hoffmantv.runeCraft.skills.attack;

import hoffmantv.runeCraft.skills.BaseStats;
import org.bukkit.entity.Player;

public class AttackStats extends BaseStats {
    private static final double XP_MULTIPLIER = 100;
    private static final double DAMAGE_XP_MULTIPLIER = 1.5;

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

    @Override
    protected double getXpMultiplier() {
        return XP_MULTIPLIER;
    }

    @Override
    protected double transformIncomingXp(double amount) {
        return amount * DAMAGE_XP_MULTIPLIER;
    }
}