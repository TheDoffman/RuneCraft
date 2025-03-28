package hoffmantv.runeCraft.skills;

import hoffmantv.runeCraft.skills.combat.PlayerCombatStatsManager;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
import hoffmantv.runeCraft.skills.fishing.FishingStatsManager;
import hoffmantv.runeCraft.skills.mining.MiningStatsManager;
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
        MiningStatsManager.loadPlayer(player);
        FishingStatsManager.loadPlayer(player);
    }
}
