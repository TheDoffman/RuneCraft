package hoffmantv.runeCraft.skills.fishing;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FishingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Process only right-click block events.
        if (!event.getAction().toString().contains("RIGHT_CLICK_BLOCK")) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        // Check that the clicked block is water.
        if (clickedBlock.getType() != Material.WATER) {
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

        // Start the fishing event.
        FishingEvent fishingEvent = new FishingEvent(player, clickedBlock);
        fishingEvent.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
    }
}