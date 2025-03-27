// Updated ArmorEquipListener.java in hoffmantv.runeCraft.combat
package hoffmantv.runeCraft.skills.combat;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        // If the clicked slot is an armor slot, schedule a check.
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            Bukkit.getScheduler().runTaskLater(RuneCraft.getInstance(), () -> checkPlayerArmor(player), 1L);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        // Check if any of the affected slots are armor slots (typically indices 36-39).
        boolean affectsArmor = event.getRawSlots().stream().anyMatch(slot -> slot >= 36 && slot <= 39);
        if (affectsArmor) {
            Bukkit.getScheduler().runTaskLater(RuneCraft.getInstance(), () -> checkPlayerArmor(player), 1L);
        }
    }

    private void checkPlayerArmor(Player player) {
        // Check helmet.
        if (!canEquip(player, player.getInventory().getHelmet())) {
            ItemStack armor = player.getInventory().getHelmet();
            player.getInventory().setHelmet(null);
            player.getInventory().addItem(armor);
            player.sendMessage("Your defence level is too low to wear that helmet! The item has been returned to your inventory.");
        }
        // Check chestplate.
        if (!canEquip(player, player.getInventory().getChestplate())) {
            ItemStack armor = player.getInventory().getChestplate();
            player.getInventory().setChestplate(null);
            player.getInventory().addItem(armor);
            player.sendMessage("Your defence level is too low to wear that chestplate! The item has been returned to your inventory.");
        }
        // Check leggings.
        if (!canEquip(player, player.getInventory().getLeggings())) {
            ItemStack armor = player.getInventory().getLeggings();
            player.getInventory().setLeggings(null);
            player.getInventory().addItem(armor);
            player.sendMessage("Your defence level is too low to wear those leggings! The item has been returned to your inventory.");
        }
        // Check boots.
        if (!canEquip(player, player.getInventory().getBoots())) {
            ItemStack armor = player.getInventory().getBoots();
            player.getInventory().setBoots(null);
            player.getInventory().addItem(armor);
            player.sendMessage("Your defence level is too low to wear those boots! The item has been returned to your inventory.");
        }
        player.updateInventory();
    }

    private boolean canEquip(Player player, ItemStack armorItem) {
        if (armorItem == null) return true;
        Material type = armorItem.getType();
        if (!isArmor(type)) return true;
        int requiredDefense = ArmorUtils.getRequiredDefense(type);
        CombatStats stats = PlayerCombatStatsManager.getStats(player);
        return stats.getDefenceLevel() >= requiredDefense;
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