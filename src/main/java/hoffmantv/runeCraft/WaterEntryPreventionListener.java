package hoffmantv.runeCraft;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WaterEntryPreventionListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null) {
            return;
        }
        Block toBlock = event.getTo().getBlock();
        Block fromBlock = event.getFrom().getBlock();
        if (toBlock.equals(fromBlock)) {
            return;
        }
        if (toBlock.getType() == Material.WATER && fromBlock.getType() != Material.WATER) {
            event.setCancelled(true);
            event.setTo(event.getFrom());
        }
    }
}
