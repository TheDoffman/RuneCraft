package hoffmantv.runeCraft.skills;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.firemaking.FiremakingListenerHelper;
import hoffmantv.runeCraft.skills.mining.MiningListenerHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitCleanupListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        RuneCraft.getInstance().getTaskRegistry().cancelPlayerTasks(playerId);
        MiningListenerHelper.clearForPlayer(playerId);
        FiremakingListenerHelper.clearForPlayer(playerId);
        PlayerSkillDataManager.saveData(RuneCraft.getInstance());
    }
}
