package hoffmantv.runeCraft.skilling.woodcutting;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skilling.woodcutting.WoodcuttingEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WoodcuttingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Process only if the action is a block click.
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;

        Material type = block.getType();
        // Check if the block is a log.
        if (type == Material.OAK_LOG || type == Material.SPRUCE_LOG ||
                type == Material.BIRCH_LOG || type == Material.JUNGLE_LOG ||
                type == Material.ACACIA_LOG || type == Material.DARK_OAK_LOG) {

            // If left-click, check that the player is holding an axe.
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                ItemStack tool = player.getInventory().getItemInMainHand();
                if (tool == null || !tool.getType().toString().endsWith("_AXE")) {
                    // If not holding an axe, do nothing.
                    return;
                }
            }

            // Cancel default interaction.
            event.setCancelled(true);
            // Start the woodcutting event.
            WoodcuttingEvent woodcuttingEvent = new WoodcuttingEvent(player, block);
            woodcuttingEvent.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
        }
    }
}