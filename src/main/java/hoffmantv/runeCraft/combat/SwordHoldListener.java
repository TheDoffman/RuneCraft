// SwordHoldListener.java in package hoffmantv.runeCraft.combat
package hoffmantv.runeCraft.combat;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skilling.CombatStats;
import hoffmantv.runeCraft.skilling.PlayerCombatStatsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;

public class SwordHoldListener implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        // Get the item in the new slot.
        ItemStack item = player.getInventory().getItem(newSlot);
        if (item == null) return;
        Material type = item.getType();
        // Check if the item is a sword.
        if (!type.toString().endsWith("_SWORD")) return;

        // Check required strength for the sword.
        int requiredStrength = SwordUtils.getRequiredStrength(type);
        CombatStats stats = PlayerCombatStatsManager.getStats(player);
        if (stats.getStrengthLevel() < requiredStrength) {
            player.sendMessage("You are not strong enough to hold that sword! Required strength: " + requiredStrength);
            int previousSlot = event.getPreviousSlot();
            // Schedule a task to revert the held slot after a tick.
            Bukkit.getScheduler().runTask(RuneCraft.getInstance(), () -> {
                player.getInventory().setHeldItemSlot(previousSlot);
            });
        }
    }
}