package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.skills.cooking.CookingStatsManager;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
import hoffmantv.runeCraft.skills.fishing.FishingStatsManager;
import hoffmantv.runeCraft.skills.mining.MiningStatsManager;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStatsManager;
import hoffmantv.runeCraft.skills.smelting.SmeltingStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SkillsCommand implements CommandExecutor {

    // Multipliers for calculating XP required for next level (customize per skill).
    private final double combatMultiplier = 50;
    private final double woodcuttingMultiplier = 50;
    private final double firemakingMultiplier = 100;
    private final double miningMultiplier = 100;
    private final double fishingMultiplier = 75;
    private final double cookingMultiplier = 50;
    private final double smeltingMultiplier = 100;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only allow players to run this command.
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;

        // Create an inventory GUI with 9 slots (adjust size if needed) and a title.
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Your Skills");

        // Add skill items.
        // We'll use custom icons:
        // Combat: IRON_SWORD, Woodcutting: WOODEN_AXE, Firemaking: FLINT_AND_STEEL,
        // Mining: IRON_PICKAXE, Fishing: FISHING_ROD, Cooking: CAKE, Smelting: FURNACE.
        inv.setItem(1, createSkillItem("Woodcutting", Material.WOODEN_AXE,
                getSkillInfo(WoodcuttingStatsManager.getStats(player), woodcuttingMultiplier)));
        inv.setItem(2, createSkillItem("Firemaking", Material.FLINT_AND_STEEL,
                getSkillInfo(FiremakingStatsManager.getStats(player), firemakingMultiplier)));
        inv.setItem(3, createSkillItem("Mining", Material.IRON_PICKAXE,
                getSkillInfo(MiningStatsManager.getStats(player), miningMultiplier)));
        inv.setItem(4, createSkillItem("Fishing", Material.FISHING_ROD,
                getSkillInfo(FishingStatsManager.getStats(player), fishingMultiplier)));
        inv.setItem(5, createSkillItem("Cooking", Material.CAKE,
                getSkillInfo(CookingStatsManager.getStats(player), cookingMultiplier)));
        inv.setItem(6, createSkillItem("Smelting", Material.FURNACE,
                getSkillInfo(SmeltingStatsManager.getStats(player), smeltingMultiplier)));

        // Open the inventory GUI for the player.
        player.openInventory(inv);
        return true;
    }

    /**
     * Helper method that creates an ItemStack representing a skill.
     *
     * @param skillName The display name of the skill.
     * @param icon      The material icon to use.
     * @param info      A list of lore lines containing the skill's information.
     * @return An ItemStack with the provided name, icon, and lore.
     */
    private ItemStack createSkillItem(String skillName, Material icon, List<String> info) {
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + skillName);
        meta.setLore(info);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Helper method that returns a List of Strings with information about the skill.
     * If stats is null, returns a default message.
     *
     * @param stats      The skill stats (must have getLevel() and getXp() methods).
     * @param multiplier The XP multiplier for the skill's leveling formula.
     * @return A list of lore strings.
     */
    private List<String> getSkillInfo(Object stats, double multiplier) {
        List<String> info = new ArrayList<>();
        if (stats == null) {
            info.add(ChatColor.RED + "Stats not loaded");
            return info;
        }
        // Using reflection to allow for different types; alternatively, define an interface for skills.
        int level = 0;
        double xp = 0;
        try {
            level = (int) stats.getClass().getMethod("getLevel").invoke(stats);
            xp = (double) stats.getClass().getMethod("getXp").invoke(stats);
        } catch (Exception e) {
            e.printStackTrace();
            info.add(ChatColor.RED + "Error retrieving stats");
            return info;
        }
        double xpForNext = multiplier * Math.pow(level, 2);
        double xpTillNext = xpForNext - xp;
        info.add(ChatColor.GRAY + "Level: " + level);
        info.add(ChatColor.GRAY + "Total XP: " + xp);
        info.add(ChatColor.GRAY + "XP till next level: " + (int) xpTillNext);
        return info;
    }
}