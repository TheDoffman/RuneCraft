package hoffmantv.runeCraft.skills.mining;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class MiningListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Process only left-click block events.
        if (!event.getAction().toString().contains("LEFT_CLICK_BLOCK")) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        // Check that the clicked block is a valid ore.
        Material oreType = clickedBlock.getType();
        if (!isOre(oreType)) return;

        // Check that the player is holding a pickaxe.
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null || !isPickaxe(heldItem.getType())) {
            player.sendMessage(ChatColor.RED + "You must hold a pickaxe to mine this ore.");
            return;
        }

        // Retrieve the player's mining stats.
        MiningStats stats = MiningStatsManager.getStats(player);
        if (stats == null) {
            player.sendMessage(ChatColor.RED + "Your mining stats are not loaded.");
            return;
        }
        int playerMiningLevel = stats.getLevel();
        int requiredLevel = MiningRequirements.getRequiredLevel(oreType);
        if (playerMiningLevel < requiredLevel) {
            player.sendMessage(ChatColor.RED + "Your mining level (" + playerMiningLevel +
                    ") is too low to mine this ore. Required: " + requiredLevel);
            return;
        }

        // Prevent multiple mining events on the same block.
        if (MiningListenerHelper.isActive(clickedBlock)) {
            player.sendMessage(ChatColor.RED + "There's already an active mining event on this ore.");
            return;
        }
        MiningListenerHelper.markActive(clickedBlock);

        // Cancel default interaction.
        event.setCancelled(true);

        // Start the mining event (simulate mining swings, etc.)
        MiningEvent miningEvent = new MiningEvent(player, clickedBlock);
        miningEvent.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
    }

    // Helper method to check if a material is an ore.
    private boolean isOre(Material material) {
        switch (material) {
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

    // Helper method to check if a material is a pickaxe.
    private boolean isPickaxe(Material material) {
        switch (material) {
            case WOODEN_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLDEN_PICKAXE:
            case DIAMOND_PICKAXE:
            case NETHERITE_PICKAXE:
                return true;
            default:
                return false;
        }
    }
}