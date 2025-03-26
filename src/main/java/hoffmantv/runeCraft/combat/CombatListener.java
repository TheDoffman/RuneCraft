// Updated CombatListener.java
package hoffmantv.runeCraft.combat;

import hoffmantv.runeCraft.RuneCraft;
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
        // Only allow players to trigger turn-based combat
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        // Ensure the player is holding a sword in their main hand.
        if (attacker.getInventory().getItemInMainHand() == null ||
                !isSword(attacker.getInventory().getItemInMainHand().getType())) {
            attacker.sendMessage("You must use a sword to attack!");
            event.setCancelled(true);
            return;
        }

        // Allow attacks on any mob (any LivingEntity that isn't a Player)
        if (!(event.getEntity() instanceof LivingEntity) || (event.getEntity() instanceof Player)) {
            attacker.sendMessage("You cannot initiate turn-based combat on that target!");
            event.setCancelled(true);
            return;
        }

        // Cancel the default damage event to start turn-based combat.
        event.setCancelled(true);

        // Retrieve the base damage based on the sword type.
        Material swordType = attacker.getInventory().getItemInMainHand().getType();
        double swordDamage = SwordUtils.getSwordDamage(swordType);

        // Start the turn-based combat session using the plugin instance.
        TurnBasedCombatManager.startSession(attacker, (LivingEntity) event.getEntity(), swordDamage, plugin);
    }

    // Helper method to determine if the provided material is a sword.
    private boolean isSword(Material material) {
        return material.toString().endsWith("_SWORD");
    }
}