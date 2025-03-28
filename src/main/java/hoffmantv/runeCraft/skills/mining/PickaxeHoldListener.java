package hoffmantv.runeCraft.skills.mining;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PickaxeHoldListener implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        ItemStack newItem = player.getInventory().getItem(newSlot);
        if (newItem == null) return;

        Material type = newItem.getType();
        if (!isPickaxe(type)) return;

        // Determine required mining level for the pickaxe type.
        int requiredLevel = getRequiredPickaxeLevel(type);

        MiningStats stats = MiningStatsManager.getStats(player);
        if (stats == null) return;
        int playerMiningLevel = stats.getLevel();

        if (playerMiningLevel < requiredLevel) {
            player.sendMessage("Your mining level (" + playerMiningLevel + ") is too low to use this pickaxe. Required: " + requiredLevel);
            int previousSlot = event.getPreviousSlot();
            Bukkit.getScheduler().runTask(RuneCraft.getInstance(), () -> {
                player.getInventory().setHeldItemSlot(previousSlot);
            });
        }
    }

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

    private int getRequiredPickaxeLevel(Material material) {
        // Define the mining level required for each pickaxe type.
        switch (material) {
            case WOODEN_PICKAXE:
                return 1;
            case STONE_PICKAXE:
                return 10;
            case IRON_PICKAXE:
                return 20;
            case GOLDEN_PICKAXE:
                return 30;
            case DIAMOND_PICKAXE:
                return 40;
            case NETHERITE_PICKAXE:
                return 50;
            default:
                return 1;
        }
    }
}