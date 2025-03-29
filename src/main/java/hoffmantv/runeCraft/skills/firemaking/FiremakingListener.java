package hoffmantv.runeCraft.skills.firemaking;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;
import java.util.Random;

public class FiremakingListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Process only right-click block events.
        if (!event.getAction().toString().contains("RIGHT_CLICK_BLOCK")) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        // Only account for logs that the player has in hand.
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null || !isLog(heldItem.getType())) {
            return;
        }
        // Use the held log type as the log type to burn.
        Material logType = heldItem.getType();

        // Retrieve the player's firemaking stats and check required level.
        FiremakingStats stats = FiremakingStatsManager.getStats(player);
        if (stats == null) {
            player.sendMessage(ChatColor.RED + "Your firemaking stats are not loaded.");
            return;
        }
        int playerFireLevel = stats.getLevel();
        int requiredLevel = FiremakingRequirements.getRequiredLevel(logType);
        if (playerFireLevel < requiredLevel) {
            player.sendMessage(ChatColor.RED + "Your firemaking level (" + playerFireLevel +
                    ") is too low to burn this log. Required: " + requiredLevel);
            return;
        }

        // Check that the player has flint and steel in their inventory.
        if (!player.getInventory().contains(Material.FLINT_AND_STEEL)) {
            player.sendMessage(ChatColor.RED + "You need a flint and steel in your inventory to start a fire.");
            return;
        }

        // Prevent multiple firemaking events on the same block.
        if (FiremakingListenerHelper.isActive(clickedBlock)) {
            player.sendMessage(ChatColor.RED + "There's already an active firemaking event at that location. The log has been returned to you.");
            // Return the log to the player's inventory.
            player.getInventory().addItem(new ItemStack(logType, 1));
            return;
        }
        FiremakingListenerHelper.markActive(clickedBlock);

        // Cancel default interaction.
        event.setCancelled(true);

        // Remove one log from the player's hand.
        int amount = heldItem.getAmount();
        heldItem.setAmount(amount - 1);
        player.sendMessage(ChatColor.GRAY + "You place the log and prepare to set it on fire...");

        // Start a repeating task to play smoke and flame particles and a flint ignite sound.
        BukkitTask particleTask = Bukkit.getScheduler().runTaskTimer(RuneCraft.getInstance(), () -> {
            clickedBlock.getWorld().spawnParticle(
                    org.bukkit.Particle.SMOKE,
                    clickedBlock.getLocation().add(0.5, 1, 0.5),
                    5, 0.2, 0.2, 0.2, 0.01);
            clickedBlock.getWorld().spawnParticle(
                    org.bukkit.Particle.FLAME,
                    clickedBlock.getLocation().add(0.5, 1, 0.5),
                    5, 0.2, 0.2, 0.2, 0.01);
        }, 0L, 10L);

        BukkitTask soundTask = Bukkit.getScheduler().runTaskTimer(RuneCraft.getInstance(), () -> {
            clickedBlock.getWorld().playSound(
                    clickedBlock.getLocation(),
                    Sound.ITEM_FLINTANDSTEEL_USE,
                    1.0F, 1.0F);
        }, 0L, 20L);

        // Schedule the firemaking event after a random delay between 1 and 5 seconds.
        int delayToLight = (random.nextInt(5) + 1) * 20; // 20-100 ticks
        Bukkit.getScheduler().runTaskLater(RuneCraft.getInstance(), () -> {
            // Cancel the particle and sound tasks.
            particleTask.cancel();
            soundTask.cancel();

            final Block campfireBlock;
            if (clickedBlock.getRelative(0, 1, 0).getType() == Material.AIR) {
                campfireBlock = clickedBlock.getRelative(0, 1, 0);
            } else {
                campfireBlock = clickedBlock;
            }
            campfireBlock.setType(Material.CAMPFIRE);
            player.sendMessage(ChatColor.GREEN + "The log is lit and a campfire has been created!");
            player.playSound(campfireBlock.getLocation(), Sound.BLOCK_CAMPFIRE_CRACKLE, 1.0F, 1.0F);

            // Award firemaking XP.
            double xpReward = FiremakingRequirements.getXpReward(logType);
            stats.addExperience(xpReward, player);
            stats.save(player);

            // Schedule campfire burnout after a random delay between 60 and 90 seconds.
            int burnTime = (random.nextInt(31) + 60) * 20; // ticks (60-90 sec)
            Bukkit.getScheduler().runTaskLater(RuneCraft.getInstance(), () -> {
                if (campfireBlock.getType() == Material.CAMPFIRE) {
                    campfireBlock.setType(Material.AIR);
                    player.sendMessage(ChatColor.YELLOW + "The campfire has burned out.");

                    // Drop a single gunpowder item named "Ash" with a lore description.
                    ItemStack ash = new ItemStack(Material.GUNPOWDER, 1);
                    ItemMeta meta = ash.getItemMeta();
                    meta.setDisplayName(ChatColor.GRAY + "Ash");
                    meta.setLore(java.util.Arrays.asList(ChatColor.DARK_GRAY + "The remains of a burned campfire."));
                    ash.setItemMeta(meta);
                    campfireBlock.getWorld().dropItem(campfireBlock.getLocation(), ash);
                }
                // Unmark the block so another event can be started later.
                FiremakingListenerHelper.unmarkActive(clickedBlock);
            }, burnTime);

        }, delayToLight);
    }

    // Helper method: check if a material is one of the log types.
    private boolean isLog(Material material) {
        String name = material.toString();
        return name.endsWith("_LOG") && !name.contains("LEAVES");
    }
}