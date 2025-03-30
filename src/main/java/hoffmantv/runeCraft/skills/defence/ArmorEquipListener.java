package hoffmantv.runeCraft.skills.defence;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        // Check if the clicked slot contains armor.
        ItemStack current = event.getCurrentItem();
        if (current == null) return;
        Material type = current.getType();
        if (!isArmor(type)) return;

        int requiredDefence = ArmorRequirements.getRequiredDefence(type);
        DefenceStats stats = DefenceStatsManager.getStats(player);
        if (stats == null) return;

        int playerDefence = stats.getLevel();
        if (playerDefence < requiredDefence) {
            player.sendMessage(ChatColor.RED + "Your Defence level (" + playerDefence +
                    ") is too low to equip this armor. Required: " + requiredDefence);
            event.setCancelled(true);
        }
    }

    private boolean isArmor(Material material) {
        String name = material.toString();
        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") ||
                name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }
}