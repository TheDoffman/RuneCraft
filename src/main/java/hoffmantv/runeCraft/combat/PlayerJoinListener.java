package hoffmantv.runeCraft.combat;

import hoffmantv.runeCraft.skilling.woodcutting.WoodcuttingStatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import hoffmantv.runeCraft.skilling.PlayerCombatStatsManager;

// PlayerJoinListener.java
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerCombatStatsManager.loadPlayer(event.getPlayer());
        WoodcuttingStatsManager.loadPlayer(player);
    }
}
