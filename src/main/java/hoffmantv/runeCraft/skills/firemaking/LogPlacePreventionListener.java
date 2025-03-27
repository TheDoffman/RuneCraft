package hoffmantv.runeCraft.skills.firemaking;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class LogPlacePreventionListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item == null) return;
        Material material = item.getType();
        // Check if the item is a log (ends with _LOG and not a leaf)
        if (material.toString().endsWith("_LOG") && !material.toString().contains("LEAVES")) {
            event.setCancelled(true);
        }
    }
}