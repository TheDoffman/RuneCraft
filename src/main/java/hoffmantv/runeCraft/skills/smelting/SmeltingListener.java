package hoffmantv.runeCraft.skills.smelting;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class SmeltingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Only process right-click block events.
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        // Check that the clicked block is a furnace or blast furnace.
        if (clickedBlock.getType() != Material.FURNACE && clickedBlock.getType() != Material.BLAST_FURNACE)
            return;

        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null) return;

        Material rawMaterial = heldItem.getType();
        // Check if the item is smeltable using your requirements class.
        int requiredLevel = SmeltingRequirements.getRequiredLevel(rawMaterial);
        SmeltingStats stats = SmeltingStatsManager.getStats(player);
        if (stats == null) {
            player.sendMessage(ChatColor.RED + "Your smelting stats are not loaded.");
            return;
        }
        if (stats.getLevel() < requiredLevel) {
            player.sendMessage(ChatColor.RED + "Your smelting level (" + stats.getLevel() +
                    ") is too low to smelt this item. Required: " + requiredLevel);
            return;
        }

        // Cancel default furnace interaction.
        event.setCancelled(true);
        player.sendMessage(ChatColor.GRAY + "You insert the ore into the furnace...");

        // Capture the raw item's custom display name (if any).
        String rawDisplayName = null;
        ItemMeta rawMeta = heldItem.getItemMeta();
        if (rawMeta != null && rawMeta.hasDisplayName()) {
            rawDisplayName = rawMeta.getDisplayName();
        }
        final String finalRawDisplayName = rawDisplayName; // Mark as final for inner class use

        // Remove one raw item from the player's hand.
        heldItem.setAmount(heldItem.getAmount() - 1);

        // Calculate dynamic smelting time.
        int baseTime = 100;  // Base time in ticks (e.g., 100 ticks = 5 seconds)
        int bonusReduction = stats.getLevel() / 2; // Example: reduce time as level increases.
        int smeltingTime = Math.max(40, baseTime - bonusReduction); // Minimum 40 ticks (2 seconds)

        // Schedule the smelting process after the calculated delay.
        new BukkitRunnable() {
            @Override
            public void run() {
                // Get the smelted result from your SmeltingRequirements.
                Material result = SmeltingRequirements.getSmeltedResult(rawMaterial);
                if (result == null) {
                    player.sendMessage(ChatColor.RED + "This item cannot be smelted.");
                    return;
                }
                // Create the smelted item.
                ItemStack smeltedItem = new ItemStack(result, 1);
                ItemMeta meta = smeltedItem.getItemMeta();
                String newName;
                // Use the raw display name (if available) and replace "Ore" with "Bar"
                if (finalRawDisplayName != null) {
                    newName = finalRawDisplayName.replace("Ore", "Bar");
                } else {
                    // Otherwise, use the default result name and replace _ORE with _BAR.
                    newName = result.name().replace("_ORE", "_BAR");
                    newName = ChatColor.GREEN + newName;
                }
                meta.setDisplayName(newName);
                smeltedItem.setItemMeta(meta);

                // Add the smelted item to the player's inventory.
                player.getInventory().addItem(smeltedItem);
                player.sendMessage(ChatColor.GREEN + "You smelt the ore into " + newName + "!");

                // Award smelting XP.
                double xpReward = SmeltingRequirements.getXpReward(rawMaterial);
                stats.addExperience(xpReward, player);
                stats.save(player);
            }
        }.runTaskLater(RuneCraft.getInstance(), smeltingTime);
    }
}