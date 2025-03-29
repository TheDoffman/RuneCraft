package hoffmantv.runeCraft.skills.fishing;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;

public class FishingEvent extends BukkitRunnable {

    private final Player player;
    private final Block waterBlock;  // The water block that was clicked.
    private final Random random;
    private int ticksWaiting = 0;
    private final double maxDistanceSquared = 64; // 8 blocks radius

    public FishingEvent(Player player, Block waterBlock) {
        this.player = player;
        this.waterBlock = waterBlock;
        this.random = new Random();
    }

    @Override
    public void run() {
        // Cancel if the player moves too far away.
        if (player.getLocation().distanceSquared(waterBlock.getLocation()) > maxDistanceSquared) {
            player.sendMessage("You moved too far away. Fishing canceled!");
            cancel();
            return;
        }

        ticksWaiting++;
        // Simulate fishing: animate and play water splash sound.
        player.swingMainHand();
        player.playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 1.0F, 1.0F);
        waterBlock.getWorld().spawnParticle(Particle.SPLASH, waterBlock.getLocation().add(0.5, 1, 0.5),
                5, 0.2, 0.2, 0.2, 0.01);

        // After a minimum wait, chance to catch a fish.
        if (ticksWaiting >= 4 && random.nextDouble() < 0.3) {
            finishFishing();
            cancel();
        }
    }

    private void finishFishing() {
        // Retrieve the player's fishing stats.
        FishingStats stats = FishingStatsManager.getStats(player);
        if (stats == null) return;
        // Determine which fish to catch based on the player's fishing level.
        FishingRequirements.FishDrop drop = FishingRequirements.getFishDrop(stats.getLevel());

        // Award XP.
        stats.addExperience(drop.xpReward, player);
        stats.save(player);

        // Create the custom fish item.
        ItemStack fishItem = new ItemStack(drop.dropMaterial, drop.amount);
        ItemMeta meta = fishItem.getItemMeta();
        meta.setDisplayName(drop.displayName);

        // Create a unique, invisible string using zero-width spaces.
        // (For example, three zero-width spaces; adjust as needed.)
        String uniqueInvisible = "\u200B\u200B\u200B";
        meta.setLore(java.util.Collections.singletonList(uniqueInvisible));

        fishItem.setItemMeta(meta);

        // Add the fish directly to the player's inventory.
        player.getInventory().addItem(fishItem);
        player.sendMessage("You caught a " + drop.displayName + "!");
    }
}