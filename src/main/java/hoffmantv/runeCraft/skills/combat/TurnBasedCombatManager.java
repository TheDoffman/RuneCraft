// Updated TurnBasedCombatManager.java
package hoffmantv.runeCraft.skills.combat;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TurnBasedCombatManager {
    // Map to store active combat sessions for players.
    private static final Map<Player, TurnBasedCombatSession> sessions = new HashMap<>();

    public static void startSession(Player player, LivingEntity target, double baseDamage, RuneCraft plugin) {
        if (sessions.containsKey(player)) {
            player.sendMessage("You are already in a combat session.");
            return;
        }
        TurnBasedCombatSession session = new TurnBasedCombatSession(player, target, baseDamage, plugin);
        sessions.put(player, session);
        session.startCombat();
    }

    public static boolean isInSession(Player player) {
        return sessions.containsKey(player);
    }

    public static TurnBasedCombatSession getSession(Player player) {
        return sessions.get(player);
    }

    public static void endSession(Player player) {
        sessions.remove(player);
    }
}