package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class TurnBasedCombatListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // Process only if the right-clicked entity is a mob (LivingEntity) but not a player.
        if (!(event.getRightClicked() instanceof LivingEntity)) return;
        if (event.getRightClicked() instanceof Player) return;

        Player player = event.getPlayer();

        // Check that the player is holding a sword.
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null || !isSword(heldItem.getType())) {
            player.sendMessage(ChatColor.RED + "You need a sword to engage in combat.");
            return;
        }

        LivingEntity mob = (LivingEntity) event.getRightClicked();
        player.sendMessage(ChatColor.GRAY + "You engage the " + mob.getName() + " in turn-based combat!");
        TurnBasedCombatSession session = new TurnBasedCombatSession(player, mob);
        session.runTaskTimer(RuneCraft.getInstance(), 0L, 20L);
    }

    // Helper method: checks if the material is a sword.
    private boolean isSword(Material material) {
        String name = material.toString();
        return name.endsWith("_SWORD");
    }
}