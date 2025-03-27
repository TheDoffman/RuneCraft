package hoffmantv.runeCraft.skills.woodcutting;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;

public class AxeHoldListener implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        ItemStack newItem = player.getInventory().getItem(newSlot);
        if (newItem == null) return;

        Material type = newItem.getType();
        // Check if the new held item is an axe.
        if (!isAxe(type)) return;

        // Get the required woodcutting level for this axe.
        int requiredLevel = hoffmantv.runeCraft.skills.woodcutting.WoodcuttingRequirements.getRequiredAxeLevel(type);
        WoodcuttingStats stats = WoodcuttingStatsManager.getStats(player);
        if (stats == null) return;
        int woodLevel = stats.getLevel();

        if (woodLevel < requiredLevel) {
            player.sendMessage("Your woodcutting level (" + woodLevel +
                    ") is too low to equip this axe. Required: " + requiredLevel);
            int previousSlot = event.getPreviousSlot();
            // Schedule a task to revert the held slot after a tick.
            Bukkit.getScheduler().runTask(RuneCraft.getInstance(), () -> {
                player.getInventory().setHeldItemSlot(previousSlot);
            });
        }
    }

    private boolean isAxe(Material material) {
        switch (material) {
            case WOODEN_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case DIAMOND_AXE:
            case NETHERITE_AXE:
                return true;
            default:
                return false;
        }
    }
}