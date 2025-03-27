package hoffmantv.runeCraft.combat;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.CombatStats;
import hoffmantv.runeCraft.skills.PlayerCombatStatsManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final RuneCraft plugin;

    public CombatListener(RuneCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Only allow players to initiate combat.
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        // Check if player is holding an item.
        if (player.getInventory().getItemInMainHand() == null) {
            player.sendMessage("You must hold a sword to attack!");
            event.setCancelled(true);
            return;
        }

        Material heldItem = player.getInventory().getItemInMainHand().getType();
        // Check if the held item is a sword.
        if (!heldItem.toString().endsWith("_SWORD")) {
            player.sendMessage("You must use a sword to attack!");
            event.setCancelled(true);
            return;
        }

        // Check if the player's strength level is high enough to use this sword.
        int requiredStrength = SwordUtils.getRequiredStrength(heldItem);
        CombatStats stats = PlayerCombatStatsManager.getStats(player);
        if (stats.getStrengthLevel() < requiredStrength) {
            player.sendMessage("Your strength level (" + stats.getStrengthLevel() +
                    ") is too low to effectively use this sword. You need at least " + requiredStrength + "!");
            event.setCancelled(true);
            return;
        }

        // Retrieve the base sword damage.
        double swordDamage = SwordUtils.getSwordDamage(heldItem);

        // If the target is a LivingEntity and not a player, start turn-based combat.
        if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Player)) {
            LivingEntity target = (LivingEntity) event.getEntity();
            event.setCancelled(true);  // Cancel the default damage event.
            // Start the turn-based combat session (make sure TurnBasedCombatManager.startSession is implemented).
            TurnBasedCombatManager.startSession(player, target, swordDamage, plugin);
        } else {
            // Otherwise, apply normal damage.
            event.setDamage(swordDamage);
        }
    }
}