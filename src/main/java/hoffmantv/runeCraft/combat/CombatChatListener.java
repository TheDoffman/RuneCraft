package hoffmantv.runeCraft.combat;

import hoffmantv.runeCraft.skilling.CombatStats;
import hoffmantv.runeCraft.skilling.PlayerCombatStatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class CombatChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        CombatStats stats = PlayerCombatStatsManager.getStats(player);
        int rank = stats.getCombatLevel();
        // Prepend the combat rank to the player's name in chat.
        // For example: "[15] Daniel: Hello world"
        String newFormat = "[" + rank + "] %s: %s";
        event.setFormat(newFormat);
    }
}