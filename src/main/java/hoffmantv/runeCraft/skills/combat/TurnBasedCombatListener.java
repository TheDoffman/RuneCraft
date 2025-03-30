package hoffmantv.runeCraft.skills.combat;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TurnBasedCombatListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // This handles right-click interactions.
        if (!(event.getRightClicked() instanceof LivingEntity)) return;
        if (event.getRightClicked() instanceof Player) return;

        Player player = event.getPlayer();
        if (TurnBasedCombatSession.isInCombat(player)) {
            player.sendMessage(ChatColor.RED + "You are already in combat!");
            return;
        }

        LivingEntity mob = (LivingEntity) event.getRightClicked();
        player.sendMessage(ChatColor.GRAY + "You engage the " + mob.getName() + " in turn-based combat!");
        TurnBasedCombatSession session = new TurnBasedCombatSession(player, mob);
        session.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // This handles left-click attacks.
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof Player) return;

        Player player = (Player) event.getDamager();
        if (TurnBasedCombatSession.isInCombat(player)) {
            return; // Already in combat.
        }
        // Cancel the normal damage event so that no regular damage is applied.
        event.setCancelled(true);

        LivingEntity mob = (LivingEntity) event.getEntity();
        player.sendMessage(ChatColor.GRAY + "You engage the " + mob.getName() + " in turn-based combat!");
        TurnBasedCombatSession session = new TurnBasedCombatSession(player, mob);
        session.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
    }
}