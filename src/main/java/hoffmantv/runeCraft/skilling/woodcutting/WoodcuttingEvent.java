package hoffmantv.runeCraft.skilling.woodcutting;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WoodcuttingEvent extends BukkitRunnable {

    private final Player player;
    private final Block initialBlock; // The log block that started the event.
    private final Material logType;
    private final Random random;
    private int ticksChopped = 0;
    // Save the tree shape (each block's location and its material).
    private final Map<Location, Material> treeShape = new HashMap<>();

    public WoodcuttingEvent(Player player, Block initialBlock) {
        this.player = player;
        this.initialBlock = initialBlock;
        this.logType = initialBlock.getType();
        this.random = new Random();
    }

    @Override
    public void run() {
        // Simulate a chopping swing.
        ticksChopped++;
        // Swing player's arm every tick.
        player.swingMainHand();
        // Play a chopping sound every tick.
        player.playSound(player.getLocation(), Sound.BLOCK_WOOD_HIT, 1.0F, 1.0F);

        // After a minimum number of swings, each additional swing has a chance to finish chopping.
        if (ticksChopped >= 3 && random.nextDouble() < 0.3) {
            finishChop();
            cancel();
        }
    }

    private void finishChop() {
        // Flood-fill the tree blocks (logs and leaves) starting from the initial block.
        Set<Block> treeBlocks = getConnectedTreeBlocks(initialBlock);
        // Save the tree shape.
        treeShape.clear();
        for (Block b : treeBlocks) {
            treeShape.put(b.getLocation(), b.getType());
        }
        // Remove the tree blocks.
        for (Block b : treeBlocks) {
            b.setType(Material.AIR);
        }

        // Play a tree falling sound.
        player.playSound(initialBlock.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.0F, 1.0F);

        // Give the player one log item.
        ItemStack logItem = new ItemStack(logType, 1);
        player.getInventory().addItem(logItem);

        // Place a sapling at the initial block's location.
        Material sapling = getSaplingForLog(logType);
        initialBlock.setType(sapling);

        // Award woodcutting XP based on the log type.
        double xpReward = WoodcuttingUtils.getXpReward(logType);
        WoodcuttingStats stats = WoodcuttingStatsManager.getStats(player);
        if (stats != null) {
            stats.addExperience(xpReward, player);
            stats.save(player);
        }

        // Show XP earned above the hotbar using the Adventure API's action bar.
        // Make sure your project is set up to use the Adventure API.
        ((net.kyori.adventure.audience.Audience) player)
                .sendActionBar(net.kyori.adventure.text.Component.text("Woodcutting XP Earned: " + xpReward)
                        .color(net.kyori.adventure.text.format.NamedTextColor.YELLOW));

        player.sendMessage("The tree has been chopped down!");

        // Schedule regrowth: after a random delay between 1 and 30 seconds.
        int delay = (random.nextInt(30) + 1) * 20; // in ticks
        Bukkit.getScheduler().runTaskLater(RuneCraft.getInstance(), () -> {
            // Check that the sapling is still there.
            if (initialBlock.getType() == sapling) {
                // Regrow the tree by setting each block in the saved tree shape.
                for (Map.Entry<Location, Material> entry : treeShape.entrySet()) {
                    Location loc = entry.getKey();
                    Material mat = entry.getValue();
                    loc.getBlock().setType(mat);
                }
            }
        }, delay);
    }

    // Flood-fill algorithm to collect all connected blocks that are part of the tree.
    private Set<Block> getConnectedTreeBlocks(Block start) {
        Set<Block> visited = new HashSet<>();
        Set<Block> toVisit = new HashSet<>();
        toVisit.add(start);

        while (!toVisit.isEmpty()) {
            Block current = toVisit.iterator().next();
            toVisit.remove(current);
            if (visited.contains(current)) continue;
            visited.add(current);

            // If this block is part of the tree, add its adjacent blocks.
            if (isTreeBlock(current)) {
                for (Block neighbor : getAdjacentBlocks(current)) {
                    if (!visited.contains(neighbor)) {
                        toVisit.add(neighbor);
                    }
                }
            }
        }
        return visited;
    }

    private boolean isTreeBlock(Block b) {
        Material type = b.getType();
        return isLog(type) || isLeaf(type);
    }

    private boolean isLog(Material type) {
        return type == Material.OAK_LOG || type == Material.SPRUCE_LOG ||
                type == Material.BIRCH_LOG || type == Material.JUNGLE_LOG ||
                type == Material.ACACIA_LOG || type == Material.DARK_OAK_LOG;
    }

    private boolean isLeaf(Material type) {
        return type == Material.OAK_LEAVES || type == Material.SPRUCE_LEAVES ||
                type == Material.BIRCH_LEAVES || type == Material.JUNGLE_LEAVES ||
                type == Material.ACACIA_LEAVES || type == Material.DARK_OAK_LEAVES;
    }

    // Get adjacent blocks in 6 directions.
    private Set<Block> getAdjacentBlocks(Block b) {
        Set<Block> blocks = new HashSet<>();
        Location loc = b.getLocation();
        blocks.add(loc.clone().add(1, 0, 0).getBlock());
        blocks.add(loc.clone().add(-1, 0, 0).getBlock());
        blocks.add(loc.clone().add(0, 1, 0).getBlock());
        blocks.add(loc.clone().add(0, -1, 0).getBlock());
        blocks.add(loc.clone().add(0, 0, 1).getBlock());
        blocks.add(loc.clone().add(0, 0, -1).getBlock());
        return blocks;
    }

    // Map a log type to its associated sapling.
    private Material getSaplingForLog(Material log) {
        switch (log) {
            case OAK_LOG:
                return Material.OAK_SAPLING;
            case SPRUCE_LOG:
                return Material.SPRUCE_SAPLING;
            case BIRCH_LOG:
                return Material.BIRCH_SAPLING;
            case JUNGLE_LOG:
                return Material.JUNGLE_SAPLING;
            case ACACIA_LOG:
                return Material.ACACIA_SAPLING;
            case DARK_OAK_LOG:
                return Material.DARK_OAK_SAPLING;
            default:
                return Material.OAK_SAPLING;
        }
    }
}