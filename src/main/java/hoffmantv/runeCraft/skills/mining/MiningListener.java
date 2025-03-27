package hoffmantv.runeCraft.skills.mining;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MiningListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();
        // Check if the block is an ore we care about.
        if (!isOre(type)) {
            return;
        }

        Player player = event.getPlayer();
        MiningStats stats = MiningStatsManager.getStats(player);
        if (stats == null) {
            player.sendMessage(ChatColor.RED + "Your mining stats are not loaded.");
            event.setCancelled(true);
            return;
        }
        int playerMiningLevel = stats.getLevel();
        int requiredLevel = MiningRequirements.getRequiredLevel(type);
        if (playerMiningLevel < requiredLevel) {
            player.sendMessage(ChatColor.RED + "Your mining level (" + playerMiningLevel +
                    ") is too low to mine this ore. Required: " + requiredLevel);
            event.setCancelled(true);
            return;
        }

        // Award XP for mining the ore.
        double xpReward = MiningRequirements.getXpReward(type);
        stats.addExperience(xpReward, player);
        stats.save(player);
        // Let the normal drops occur.
    }

    private boolean isOre(Material material) {
        switch(material) {
            case COAL_ORE:
            case IRON_ORE:
            case GOLD_ORE:
            case REDSTONE_ORE:
            case LAPIS_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case NETHER_QUARTZ_ORE:
                return true;
            default:
                return false;
        }
    }
}