package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TurnBasedCombatListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // Only process if the right-clicked entity is a mob (LivingEntity) but not a player.
        if (!(event.getRightClicked() instanceof LivingEntity)) return;
        if (event.getRightClicked() instanceof Player) return;

        Player player = event.getPlayer();
        LivingEntity mob = (LivingEntity) event.getRightClicked();

        // Optionally, add a check to prevent starting multiple sessions.
        // For now, we'll assume one session per interaction.

        player.sendMessage(ChatColor.GRAY + "You engage the " + mob.getName() + " in turn-based combat!");
        TurnBasedCombatSession session = new TurnBasedCombatSession(player, mob);
        session.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
    }
}