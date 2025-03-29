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
import org.bukkit.scheduler.BukkitRunnable;

public class SmeltingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Process only right-click block events.
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        // Check that the clicked block is a furnace or blast furnace.
        Material blockType = clickedBlock.getType();
        if (blockType != Material.FURNACE && blockType != Material.BLAST_FURNACE) {
            return;
        }

        // Check that the player is holding a smeltable item.
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null || !isSmeltable(heldItem.getType())) {
            player.sendMessage(ChatColor.RED + "You must hold a smeltable item (e.g., IRON_ORE or GOLD_ORE) to use the furnace.");
            return;
        }
        Material rawMaterial = heldItem.getType();

        // Retrieve the player's smelting stats.
        SmeltingStats stats = SmeltingStats.load(player); // Alternatively, use a SmeltingStatsManager.
        if (stats == null) {
            player.sendMessage(ChatColor.RED + "Your smelting stats are not loaded.");
            return;
        }
        int playerSmeltLevel = stats.getLevel();
        int requiredLevel = SmeltingRequirements.getRequiredLevel(rawMaterial);
        if (playerSmeltLevel < requiredLevel) {
            player.sendMessage(ChatColor.RED + "Your smelting level (" + playerSmeltLevel +
                    ") is too low to smelt this item. Required: " + requiredLevel);
            return;
        }

        // Cancel default furnace interaction.
        event.setCancelled(true);
        player.sendMessage(ChatColor.GRAY + "You insert the item into the furnace...");

        // Remove one raw item from the player's hand.
        int amount = heldItem.getAmount();
        heldItem.setAmount(amount - 1);

        // Schedule the smelting process after a delay (simulate smelting time).
        new BukkitRunnable() {
            @Override
            public void run() {
                // Get the smelted result from SmeltingRequirements.
                Material result = SmeltingRequirements.getSmeltedResult(rawMaterial);
                if (result == null) {
                    player.sendMessage(ChatColor.RED + "This item cannot be smelted.");
                    return;
                }
                // Create the smelted item.
                ItemStack smeltedItem = new ItemStack(result, 1);
                // Optionally, you could add a custom display name here.

                // Add the smelted item to the player's inventory.
                player.getInventory().addItem(smeltedItem);
                player.sendMessage(ChatColor.GREEN + "The item has been smelted into " + result.name() + "!");

                // Award smelting XP.
                double xpReward = SmeltingRequirements.getXpReward(rawMaterial);
                stats.addExperience(xpReward, player);
                stats.save(player);
            }
        }.runTaskLater(RuneCraft.getInstance(), 60L); // Delay of 60 ticks (3 seconds)
    }

    // Helper method: check if the material is smeltable.
    private boolean isSmeltable(Material material) {
        switch (material) {
            case IRON_ORE:
            case GOLD_ORE:
            case NETHER_QUARTZ_ORE:
                return true;
            // Optionally add more smeltable items here.
            default:
                return false;
        }
    }
}