package hoffmantv.runeCraft.skills.smithing;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SmithingMenu {

    private static final String MENU_TITLE = ChatColor.DARK_GRAY + "Smithing Menu";

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, MENU_TITLE);

        SmithingStats stats = SmithingStatsManager.getStats(player);
        if (stats == null) return;

        int playerLevel = stats.getLevel();

        // Add Smithable Items
        addSmithingItem(menu, Material.IRON_SWORD, 20, playerLevel, "Iron Sword");
        addSmithingItem(menu, Material.IRON_PICKAXE, 20, playerLevel, "Iron Pickaxe");
        addSmithingItem(menu, Material.IRON_CHESTPLATE, 33, playerLevel, "Iron Chestplate");
        addSmithingItem(menu, Material.GOLDEN_SWORD, 30, playerLevel, "Golden Sword");
        addSmithingItem(menu, Material.GOLDEN_PICKAXE, 30, playerLevel, "Golden Pickaxe");
        addSmithingItem(menu, Material.GOLDEN_CHESTPLATE, 48, playerLevel, "Golden Chestplate");
        addSmithingItem(menu, Material.DIAMOND_SWORD, 60, playerLevel, "Diamond Sword");
        addSmithingItem(menu, Material.DIAMOND_PICKAXE, 60, playerLevel, "Diamond Pickaxe");
        addSmithingItem(menu, Material.DIAMOND_CHESTPLATE, 99, playerLevel, "Diamond Chestplate");

        player.openInventory(menu);
    }

    private static void addSmithingItem(Inventory menu, Material material, int requiredLevel, int playerLevel, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + displayName);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Requires Level: " + requiredLevel);
            if (playerLevel >= requiredLevel) {
                lore.add(ChatColor.GREEN + "Click to smith!");
            } else {
                lore.add(ChatColor.RED + "Level too low!");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        menu.addItem(item);
    }

    public static boolean isMenu(InventoryClickEvent event) {
        return event.getView().getTitle().equals(MENU_TITLE);
    }

    public static void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || !clicked.hasItemMeta()) return;

        SmithingStats stats = SmithingStatsManager.getStats(player);
        if (stats == null) return;

        int playerLevel = stats.getLevel();
        Material material = clicked.getType();
        String displayName = clicked.getItemMeta().getDisplayName();

        int requiredLevel = getRequiredLevel(material);
        if (playerLevel < requiredLevel) {
            player.sendMessage(ChatColor.RED + "You don't have the required smithing level!");
            player.closeInventory();
            return;
        }

        // Determine how many bars are needed
        int barsNeeded = getBarsRequired(material);
        Material barType = getBarMaterial(material);

        if (!hasEnoughBars(player, barType, barsNeeded)) {
            player.sendMessage(ChatColor.RED + "You need at least " + barsNeeded + " " + formatMaterialName(barType) + " to smith this!");
            player.closeInventory();
            return;
        }

        // Remove the required bars
        removeBars(player, barType, barsNeeded);

        // Give the player the item
        player.getInventory().addItem(new ItemStack(material, 1));
        player.sendMessage(ChatColor.GREEN + "You smith a " + displayName + "!");

        // Award XP
        double xpReward = barsNeeded * getXpPerBar(barType);
        stats.addExperience(xpReward, player);
        stats.save(player);

        // Play sound
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);

        player.closeInventory();
    }

    private static int getRequiredLevel(Material material) {
        switch (material) {
            case IRON_SWORD: return 20;
            case IRON_PICKAXE: return 20;
            case IRON_CHESTPLATE: return 33;
            case GOLDEN_SWORD: return 30;
            case GOLDEN_PICKAXE: return 30;
            case GOLDEN_CHESTPLATE: return 48;
            case DIAMOND_SWORD: return 60;
            case DIAMOND_PICKAXE: return 60;
            case DIAMOND_CHESTPLATE: return 99;
            default: return 1;
        }
    }
    private static int getBarsRequired(Material material) {
        switch (material) {
            case IRON_SWORD: return 1;
            case IRON_PICKAXE: return 2;
            case IRON_CHESTPLATE: return 5;
            case GOLDEN_SWORD: return 1;
            case GOLDEN_PICKAXE: return 2;
            case GOLDEN_CHESTPLATE: return 5;
            case DIAMOND_SWORD: return 2;
            case DIAMOND_PICKAXE: return 3;
            case DIAMOND_CHESTPLATE: return 6;
            default: return 1;
        }
    }

    private static Material getBarMaterial(Material material) {
        switch (material) {
            case IRON_SWORD:
            case IRON_PICKAXE:
            case IRON_CHESTPLATE:
                return Material.IRON_INGOT;
            case GOLDEN_SWORD:
            case GOLDEN_PICKAXE:
            case GOLDEN_CHESTPLATE:
                return Material.GOLD_INGOT;
            case DIAMOND_SWORD:
            case DIAMOND_PICKAXE:
            case DIAMOND_CHESTPLATE:
                return Material.DIAMOND;
            default:
                return Material.IRON_INGOT;
        }
    }

    private static double getXpPerBar(Material barMaterial) {
        switch (barMaterial) {
            case IRON_INGOT:
                return 25.0;
            case GOLD_INGOT:
                return 37.5;
            case DIAMOND:
                return 75.0;
            default:
                return 12.5;
        }
    }

    private static boolean hasEnoughBars(Player player, Material barType, int amountNeeded) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == barType) {
                count += item.getAmount();
            }
        }
        return count >= amountNeeded;
    }

    private static void removeBars(Player player, Material barType, int amountNeeded) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (item.getType() != barType) continue;

            int itemAmount = item.getAmount();

            if (itemAmount > amountNeeded) {
                item.setAmount(itemAmount - amountNeeded);
                return;
            } else {
                amountNeeded -= itemAmount;
                item.setAmount(0);
            }

            if (amountNeeded <= 0) return;
        }
    }

    private static String formatMaterialName(Material material) {
        return material.name().replace("_", " ").toLowerCase();
    }
}