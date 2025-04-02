package hoffmantv.runeCraft.skills.smelting;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
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
        Material originalType = clickedBlock.getType();
        if (originalType != Material.FURNACE && originalType != Material.BLAST_FURNACE)
            return;

        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null) return;

        Material rawMaterial = heldItem.getType();
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

        // Preserve the original furnace orientation.
        Directional originalData = (Directional) clickedBlock.getBlockData();

        // Change the furnace to its lit variant while preserving orientation.
        Material litType = null;
        if (originalType == Material.FURNACE) {
            litType = Material.FURNACE;
        } else if (originalType == Material.FURNACE) {
            litType = Material.BLAST_FURNACE;
        }
        if (litType != null) {
            Directional litData = (Directional) Bukkit.createBlockData(litType);
            litData.setFacing(originalData.getFacing());
            clickedBlock.setBlockData(litData);
        }

        // Capture the raw item's custom display name (if any).
        String rawDisplayName = null;
        ItemMeta rawMeta = heldItem.getItemMeta();
        if (rawMeta != null && rawMeta.hasDisplayName()) {
            rawDisplayName = rawMeta.getDisplayName();
        }
        final String finalRawDisplayName = rawDisplayName; // For inner class usage.

        // Remove one raw item from the player's hand.
        heldItem.setAmount(heldItem.getAmount() - 1);

        // Calculate dynamic smelting time.
        int baseTime = 100;  // Base time in ticks (e.g., 100 ticks = 5 seconds)
        int bonusReduction = stats.getLevel() / 2; // Example: reduce time as level increases.
        int smeltingTime = Math.max(40, baseTime - bonusReduction); // Minimum 40 ticks (2 seconds)

        // Optional: Spawn continuous furnace effects while smelting.
        new BukkitRunnable() {
            int effectTicks = 0;
            @Override
            public void run() {
                if (effectTicks >= smeltingTime) {
                    cancel();
                    return;
                }
                clickedBlock.getWorld().spawnParticle(
                        org.bukkit.Particle.SMOKE,
                        clickedBlock.getLocation().add(0.5, 1, 0.5),
                        5, 0.2, 0.2, 0.2, 0.05);
                effectTicks += 10;
            }
        }.runTaskTimer(RuneCraft.getInstance(), 0L, 10L);

        // Schedule the smelting process after the calculated delay.
        new BukkitRunnable() {
            @Override
            public void run() {
                // Revert the furnace back to its original unlit state while preserving orientation.
                Directional revertData = (Directional) Bukkit.createBlockData(originalType);
                revertData.setFacing(originalData.getFacing());
                clickedBlock.setBlockData(revertData);

                // Get the smelted result.
                Material result = SmeltingRequirements.getSmeltedResult(rawMaterial);
                if (result == null) {
                    player.sendMessage(ChatColor.RED + "This item cannot be smelted.");
                    return;
                }
                // Create the smelted item.
                ItemStack smeltedItem = new ItemStack(result, 1);
                ItemMeta meta = smeltedItem.getItemMeta();
                String newName;
                if (finalRawDisplayName != null) {
                    newName = finalRawDisplayName.replace("Ore", "Bar");
                } else {
                    newName = result.name().replace("_ORE", "_BAR");
                    newName = ChatColor.GREEN + newName;
                }
                meta.setDisplayName(newName);
                // If smelting coal, set custom model data for custom texture.
                if (rawMaterial == Material.COAL) {
                    meta.setCustomModelData(1);
                }
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