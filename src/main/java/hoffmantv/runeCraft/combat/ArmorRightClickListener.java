// ArmorRightClickListener.java in package hoffmantv.runeCraft.combat
package hoffmantv.runeCraft.combat;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.CombatStats;
import hoffmantv.runeCraft.skills.PlayerCombatStatsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;

public class ArmorRightClickListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // We only care about right-click actions.
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        Material type = item.getType();
        // Check if the item is a piece of armor.
        if (!isArmor(type)) return;

        int requiredDefense = ArmorUtils.getRequiredDefense(type);
        CombatStats stats = PlayerCombatStatsManager.getStats(player);
        if (stats.getDefenceLevel() < requiredDefense) {
            player.sendMessage("Your defence level (" + stats.getDefenceLevel() +
                    ") is too low to equip that armor. Required: " + requiredDefense);
            event.setCancelled(true);
            // Schedule an inventory update to revert the equip.
            Bukkit.getScheduler().runTask(RuneCraft.getInstance(), player::updateInventory);
        }
    }

    private boolean isArmor(Material material) {
        switch (material) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS:
                return true;
            default:
                return false;
        }
    }
}