package hoffmantv.runeCraft.skills.mining;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.mining.MiningRequirements.OreDrop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiningEvent extends BukkitRunnable {

    private final Player player;
    private final Block oreBlock;
    private final Material oreType;
    private final Random random;
    private int ticksMined = 0;
    // Maximum allowed distance squared between player and ore block (8 blocks radius).
    private final double maxDistanceSquared = 64;

    public MiningEvent(Player player, Block oreBlock) {
        this.player = player;
        this.oreBlock = oreBlock;
        this.oreType = oreBlock.getType();
        this.random = new Random();
    }

    @Override
    public void run() {
        if (!player.isOnline() || player.getWorld() != oreBlock.getWorld()) {
            cleanup();
            cancel();
            return;
        }
        // Cancel if the player moves too far away.
        if (player.getLocation().distanceSquared(oreBlock.getLocation()) > maxDistanceSquared) {
            player.sendMessage("You moved too far away. Mining canceled!");
            cleanup();
            cancel();
            return;
        }

        // Simulate a mining swing.
        ticksMined++;
        player.swingMainHand();
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_HIT, 1.0F, 1.0F);
        oreBlock.getWorld().spawnParticle(Particle.FALLING_DUST,
                oreBlock.getLocation().add(0.5, 0.5, 0.5),
                5, 0.2, 0.2, 0.2, 0.01, oreType.createBlockData());

        // After a minimum number of swings, attempt to finish mining.
        if (ticksMined >= 3 && random.nextDouble() < 0.3) {
            finishMining();
            cancel();
        }
    }

    private void finishMining() {
        // Retrieve the custom OreDrop for this ore.
        OreDrop oreDrop = MiningRequirements.getOreDrop(oreType);
        if (oreDrop == null) {
            player.sendMessage("This ore cannot be mined.");
            cancel();
            return;
        }

        // Remove the ore block.
        oreBlock.setType(Material.AIR);
        oreBlock.getWorld().playSound(oreBlock.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
        player.sendMessage("You have mined the ore!");

        // Award XP to the player.
        double xpReward = oreDrop.xpReward;
        MiningStats stats = MiningStatsManager.getStats(player);
        if (stats != null) {
            stats.addExperience(xpReward, player);
            stats.save(player);
        }

        // Create the custom drop.
        ItemStack drop = new ItemStack(oreDrop.material, 1);
        ItemMeta meta = drop.getItemMeta();
        meta.setDisplayName(oreDrop.displayName);
        // Add lore for description and a unique invisible tag to prevent stacking.
        List<String> lore = new ArrayList<>();
        lore.add("A precious ore mined from the depths.");
        // The unique invisible tag ensures this item doesn't stack.
        lore.add("\u200B" + System.nanoTime());
        meta.setLore(lore);
        drop.setItemMeta(meta);

        // Drop the item naturally.
        oreBlock.getWorld().dropItemNaturally(oreBlock.getLocation(), drop);

        // Schedule ore respawn after a random delay between 5 and 10 seconds.
        int respawnTime = (random.nextInt(6) + 5) * 20; // 5-10 seconds in ticks.
        BukkitTask respawnTask = Bukkit.getScheduler().runTaskLater(RuneCraft.getInstance(), () -> {
            oreBlock.setType(oreType);
            oreBlock.getWorld().spawnParticle(Particle.SMOKE,
                    oreBlock.getLocation().add(0.5, 1, 0.5),
                    10, 0.2, 0.2, 0.2, 0.05);
            oreBlock.getWorld().playSound(oreBlock.getLocation(), Sound.BLOCK_FURNACE_FIRE_CRACKLE, 0.7F, 1.0F);
            if (player.isOnline()) {
                player.sendMessage("The ore has respawned.");
            }
            cleanup();
        }, respawnTime);
        RuneCraft.getInstance().getTaskRegistry().registerPlayerTask(player.getUniqueId(), respawnTask);
    }

    private void cleanup() {
        // Unmark the block so that a new mining event can be initiated.
        MiningListenerHelper.unmarkActive(oreBlock);
    }
}