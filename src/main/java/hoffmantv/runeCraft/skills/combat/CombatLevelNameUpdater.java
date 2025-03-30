package hoffmantv.runeCraft.skills.combat;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatLevelNameUpdater {

    /**
     * Starts a repeating task that updates every online player's display name
     * to include their current combat level.
     */
    public static void startUpdating() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    int combatLevel = CombatLevelCalculator.getCombatLevel(player);
                    // Create a new name with the player's original name plus a combat level suffix.
                    String updatedName = ChatColor.AQUA + player.getName() + ChatColor.GRAY + " [C:" + combatLevel + "]";

                    // Update the player's player list name so it appears in the tab list.
                    player.setPlayerListName(updatedName);

                    // Optionally, update the player's custom name to show above their head.
                    // (Note: Some servers or plugins might override this; you may also consider using teams.)
                    player.setCustomName(updatedName);
                    player.setCustomNameVisible(true);
                }
            }
        }.runTaskTimer(RuneCraft.getInstance(), 0L, 100L); // Update every 5 seconds (100 ticks)
    }
}