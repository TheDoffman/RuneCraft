package hoffmantv.runeCraft.skills.woodcutting;

import hoffmantv.runeCraft.RuneCraft;
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
        // Only process right- or left-click on a block.
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        Material blockType = block.getType();

        // Check if the block is a log.
        if (isLog(blockType)) {
            // Retrieve player's woodcutting stats.
            WoodcuttingStats stats = WoodcuttingStatsManager.getStats(player);
            if (stats == null) return;
            int woodLevel = stats.getLevel();

            // Check if the tree type can be cut at the player's woodcutting level.
            int requiredTreeLevel = hoffmantv.runeCraft.skills.woodcutting.WoodcuttingRequirements.getRequiredTreeLevel(blockType);
            if (woodLevel < requiredTreeLevel) {
                player.sendMessage("Your woodcutting level (" + woodLevel + ") is too low to cut this type of tree. Required: " + requiredTreeLevel);
                event.setCancelled(true);
                return;
            }

            // For left-click, require that the player is holding an axe.
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                ItemStack tool = player.getInventory().getItemInMainHand();
                if (tool == null || !isAxe(tool.getType())) {
                    // Not holding an axe—do nothing.
                    return;
                }
                int requiredAxeLevel = hoffmantv.runeCraft.skills.woodcutting.WoodcuttingRequirements.getRequiredAxeLevel(tool.getType());
                if (woodLevel < requiredAxeLevel) {
                    player.sendMessage("Your woodcutting level (" + woodLevel + ") is too low to use this axe. Required: " + requiredAxeLevel);
                    event.setCancelled(true);
                    return;
                }
            }

            // Cancel default interaction and start the woodcutting event.
            event.setCancelled(true);
            WoodcuttingEvent woodcuttingEvent = new WoodcuttingEvent(player, block);
            woodcuttingEvent.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
        }
    }

    private boolean isLog(Material material) {
        return material == Material.OAK_LOG || material == Material.SPRUCE_LOG ||
                material == Material.BIRCH_LOG || material == Material.JUNGLE_LOG ||
                material == Material.ACACIA_LOG || material == Material.DARK_OAK_LOG;
    }

    private boolean isAxe(Material material) {
        return material == Material.WOODEN_AXE || material == Material.STONE_AXE ||
                material == Material.IRON_AXE || material == Material.GOLDEN_AXE ||
                material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }
}