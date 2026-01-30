package hoffmantv.runeCraft.scoreboard;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ScoreboardCleanupListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        RuneCraft.getInstance().removeLeaderboard(playerId);
    }
}
