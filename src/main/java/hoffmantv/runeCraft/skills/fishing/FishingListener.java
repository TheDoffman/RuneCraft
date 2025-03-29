package hoffmantv.runeCraft.skills.fishing;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FishingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Process only right-click block events.
        if (!event.getAction().toString().contains("RIGHT_CLICK_BLOCK")) return;

        Player player = event.getPlayer();
        // Use getTargetBlockExact with FluidCollisionMode.ALWAYS to force detection of water blocks.
        Block targetBlock = player.getTargetBlockExact(10, FluidCollisionMode.ALWAYS);
        if (targetBlock == null) return;

        // Check that the target block is water.
        if (targetBlock.getType() != Material.WATER) return;

        // Check that the water block is a designated fishing spot.
        if (!FishingSpotsManager.isFishingSpot(targetBlock.getLocation())) {
            player.sendMessage(ChatColor.RED + "This is not a designated fishing spot.");
            return;
        }

        // Check that the player is holding a fishing rod.
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null || heldItem.getType() != Material.FISHING_ROD) {
            player.sendMessage(ChatColor.RED + "You need to hold a fishing rod to fish.");
            return;
        }

        // Cancel default interaction.
        event.setCancelled(true);
        player.sendMessage(ChatColor.GRAY + "You cast your line...");

        // Start the fishing event using the target water block.
        FishingEvent fishingEvent = new FishingEvent(player, targetBlock);
        fishingEvent.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
    }
}