// Updated CombatMovementListener.java
package hoffmantv.runeCraft.skills.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatMovementListener implements Listener {

    // Map to store the last warning timestamp for each player
    private final Map<UUID, Long> lastWarned = new HashMap<>();
    // Set a cooldown of 3 seconds (3000 milliseconds) for sending movement warnings
    private final long warningCooldownMillis = 3000;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (TurnBasedCombatManager.isInSession(player)) {
            event.setCancelled(true);
            long currentTime = System.currentTimeMillis();
            UUID playerId = player.getUniqueId();
            // Only send a warning if the cooldown has expired
            if (!lastWarned.containsKey(playerId) ||
                    (currentTime - lastWarned.get(playerId)) > warningCooldownMillis) {
                player.sendMessage("You cannot move during combat!");
                lastWarned.put(playerId, currentTime);
            }
        }
    }
}