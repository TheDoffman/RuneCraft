package hoffmantv.runeCraft.skills.mining;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class MiningEvent extends BukkitRunnable {

    private final Player player;
    private final Block oreBlock;      // The ore block that started the event.
    private final Material oreType;    // The type of ore being mined.
    private final Random random;
    private int ticksMined = 0;
    // Maximum squared distance allowed between player and ore block (8 blocks radius).
    private final double maxDistanceSquared = 64;

    public MiningEvent(Player player, Block oreBlock) {
        this.player = player;
        this.oreBlock = oreBlock;
        this.oreType = oreBlock.getType();
        this.random = new Random();
    }

    @Override
    public void run() {
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
        // Spawn ore-specific falling dust particles.
        oreBlock.getWorld().spawnParticle(Particle.FALLING_DUST, oreBlock.getLocation().add(0.5, 0.5, 0.5),
                5, 0.2, 0.2, 0.2, 0.01, oreType.createBlockData());

        // After a minimum number of swings, chance to finish mining.
        if (ticksMined >= 3 && random.nextDouble() < 0.3) {
            finishMining();
            cancel();
        }
    }

    private void finishMining() {
        // Remove the ore block.
        oreBlock.setType(Material.AIR);
        player.sendMessage("You have mined the ore!");

        // Award mining XP.
        double xpReward = MiningRequirements.getXpReward(oreType);
        MiningStats stats = MiningStatsManager.getStats(player);
        if (stats != null) {
            stats.addExperience(xpReward, player);
            stats.save(player);
        }

        // Drop the ore item.
        ItemStack drop = new ItemStack(oreType, 1);
        oreBlock.getWorld().dropItem(oreBlock.getLocation(), drop);

        // Schedule ore respawn after a random delay between 5 and 10 seconds.
        int respawnTime = (random.nextInt(6) + 5) * 20; // 5-10 seconds in ticks
        Bukkit.getScheduler().runTaskLater(RuneCraft.getInstance(), () -> {
            oreBlock.setType(oreType);
            player.sendMessage("The ore has respawned.");
            cleanup();
        }, respawnTime);
    }

    private void cleanup() {
        // Unmark the block so that a new mining event can start.
        MiningListenerHelper.unmarkActive(oreBlock);
    }
}