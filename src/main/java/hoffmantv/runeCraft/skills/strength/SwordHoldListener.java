package hoffmantv.runeCraft.skills.strength;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class SwordHoldListener implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        ItemStack newItem = player.getInventory().getItem(newSlot);
        if (newItem == null) return;

        Material type = newItem.getType();
        if (!isSword(type)) return;

        // Get the required Strength level for this sword.
        int requiredLevel = SwordRequirements.getRequiredStrength(type);
        StrengthStats stats = StrengthStatsManager.getStats(player);
        if (stats == null) return;
        int playerStrength = stats.getLevel();

        if (playerStrength < requiredLevel) {
            player.sendMessage(ChatColor.RED + "Your Strength level (" + playerStrength +
                    ") is too low to wield this sword. Required: " + requiredLevel);
            int previousSlot = event.getPreviousSlot();
            // Revert the held item to the previous slot after a short delay.
            Bukkit.getScheduler().runTask(RuneCraft.getInstance(), () -> {
                player.getInventory().setHeldItemSlot(previousSlot);
            });
        }
    }

    private boolean isSword(Material material) {
        return material.toString().endsWith("_SWORD");
    }
}