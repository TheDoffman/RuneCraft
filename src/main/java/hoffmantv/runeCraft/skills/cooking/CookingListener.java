package hoffmantv.runeCraft.skills.cooking;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class CookingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Process only right-click block events.
        if (!event.getAction().toString().contains("RIGHT_CLICK_BLOCK")) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        // Only allow cooking on campfires.
        if (clickedBlock.getType() != Material.CAMPFIRE) return;

        // Check that the player is holding a raw food item.
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null) return;
        Material rawFoodMaterial = heldItem.getType();

        // Get food data for this raw food item.
        CookingRequirements.FoodData foodData = CookingRequirements.getFoodData(rawFoodMaterial);
        if (foodData == null) {
            player.sendMessage(ChatColor.RED + "You cannot cook that item.");
            return;
        }

        // Retrieve the player's cooking stats.
        CookingStats stats = CookingStats.load(player); // Alternatively, use CookingStatsManager if available.
        if (stats == null) {
            player.sendMessage(ChatColor.RED + "Your cooking stats are not loaded.");
            return;
        }
        int playerCookingLevel = stats.getLevel();
        if (playerCookingLevel < foodData.requiredLevel) {
            player.sendMessage(ChatColor.RED + "Your cooking level (" + playerCookingLevel +
                    ") is too low to cook that. Required: " + foodData.requiredLevel);
            return;
        }

        // Cancel default interaction.
        event.setCancelled(true);
        player.sendMessage(ChatColor.GRAY + "You place the raw food on the campfire...");

        // Capture the custom display name if the raw item already has one.
        String customName = null;
        ItemMeta rawMeta = heldItem.getItemMeta();
        if (rawMeta != null && rawMeta.hasDisplayName()) {
            customName = rawMeta.getDisplayName();
        }
        final String finalCustomName = customName;  // Mark as final for inner class use

        // Remove one raw food item from the player's hand.
        int amount = heldItem.getAmount();
        heldItem.setAmount(amount - 1);

        // Schedule a cooking task to simulate cooking time (3 seconds).
        new BukkitRunnable() {
            @Override
            public void run() {
                // Create the cooked food item.
                ItemStack cookedItem = new ItemStack(foodData.cookedResult, 1);
                ItemMeta meta = cookedItem.getItemMeta();
                if (finalCustomName != null) {
                    meta.setDisplayName(finalCustomName);
                } else {
                    meta.setDisplayName(ChatColor.GREEN + foodData.displayName);
                }
                // Add a unique invisible tag to prevent stacking.
                meta.setLore(Arrays.asList("\u200B" + System.nanoTime()));
                cookedItem.setItemMeta(meta);

                // Instead of dropping the item, add it directly to the player's inventory.
                player.getInventory().addItem(cookedItem);
                player.sendMessage(ChatColor.GREEN + "Your food has been cooked into " +
                        (finalCustomName != null ? finalCustomName : foodData.displayName) + " and placed in your inventory!");

                // Award cooking XP.
                stats.addExperience(foodData.xpReward, player);
                stats.save(player);
            }
        }.runTaskLater(RuneCraft.getInstance(), 60L); // Delay of 60 ticks (3 seconds)
    }
}