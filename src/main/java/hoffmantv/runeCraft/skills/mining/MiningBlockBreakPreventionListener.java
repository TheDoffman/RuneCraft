package hoffmantv.runeCraft.skills.mining;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MiningBlockBreakPreventionListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        if (isOre(type)) {
            Player player = event.getPlayer();
            event.setCancelled(true);
        }
    }

    // Helper method to check if a block is an ore.
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
            case COPPER_ORE:
                return true;
            default:
                return false;
        }
    }
}