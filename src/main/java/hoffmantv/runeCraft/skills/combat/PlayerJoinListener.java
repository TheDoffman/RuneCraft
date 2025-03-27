package hoffmantv.runeCraft.skills.combat;

import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// PlayerJoinListener.java
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerCombatStatsManager.loadPlayer(event.getPlayer());
        WoodcuttingStatsManager.loadPlayer(player);
        FiremakingStatsManager.loadPlayer(player);
    }
}
