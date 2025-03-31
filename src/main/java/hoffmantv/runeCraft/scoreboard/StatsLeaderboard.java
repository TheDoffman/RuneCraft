package hoffmantv.runeCraft.scoreboard;

import hoffmantv.runeCraft.skills.attack.AttackStats;
import hoffmantv.runeCraft.skills.attack.AttackStatsManager;
import hoffmantv.runeCraft.skills.combat.CombatLevelCalculator;
import hoffmantv.runeCraft.skills.defence.DefenceStats;
import hoffmantv.runeCraft.skills.defence.DefenceStatsManager;
import hoffmantv.runeCraft.skills.strength.StrengthStats;
import hoffmantv.runeCraft.skills.strength.StrengthStatsManager;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStats;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStatsManager;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStats;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
import hoffmantv.runeCraft.skills.mining.MiningStats;
import hoffmantv.runeCraft.skills.mining.MiningStatsManager;
import hoffmantv.runeCraft.skills.fishing.FishingStats;
import hoffmantv.runeCraft.skills.fishing.FishingStatsManager;
import hoffmantv.runeCraft.skills.cooking.CookingStats;
import hoffmantv.runeCraft.skills.cooking.CookingStatsManager;
import hoffmantv.runeCraft.skills.smelting.SmeltingStats;
import hoffmantv.runeCraft.skills.smelting.SmeltingStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class StatsLeaderboard {
    private final Scoreboard scoreboard;
    private final Objective objective;

    public StatsLeaderboard() {
        // Create a new scoreboard and register a dummy objective for the sidebar.
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "✦ Your Skills ✦");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Updates the scoreboard with the given player's skill levels.
     * Each skill is displayed on its own line, grouped into rows of 3,
     * and at the bottom a centered line shows the total non-combat skill points.
     *
     * @param player The player whose skills will be displayed.
     */
    public void update(Player player) {
        // Clear existing entries.
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // Add a footer for visual separation.
        String footer = ChatColor.GOLD + "————————————";
        objective.getScore(footer + getInvisible(100)).setScore(0);

        int score = 99;
        int uniqueCounter = 0;

        // Retrieve skill data.
        AttackStats attackStats = AttackStatsManager.getStats(player);
        DefenceStats defenceStats = DefenceStatsManager.getStats(player);
        StrengthStats strengthStats = StrengthStatsManager.getStats(player);
        WoodcuttingStats woodStats = WoodcuttingStatsManager.getStats(player);
        FiremakingStats fireStats = FiremakingStatsManager.getStats(player);
        MiningStats miningStats = MiningStatsManager.getStats(player);
        FishingStats fishingStats = FishingStatsManager.getStats(player);
        CookingStats cookingStats = CookingStatsManager.getStats(player);
        SmeltingStats smeltingStats = SmeltingStatsManager.getStats(player);

        // Ensure all skills are loaded.
        if (attackStats == null || defenceStats == null || strengthStats == null ||
                woodStats == null || fireStats == null || miningStats == null ||
                fishingStats == null || cookingStats == null || smeltingStats == null) {
            return;
        }

        // Optionally, display the player's name.
        // String nameLine = ChatColor.AQUA + player.getName() + getInvisible(uniqueCounter++);
        // objective.getScore(nameLine).setScore(score--);

        // Display Combat Rank on a separate line.
        String combatRankLine = ChatColor.GRAY + "Combat Rank: " + CombatLevelCalculator.getCombatLevel(player)
                + getInvisible(uniqueCounter++);
        objective.getScore(combatRankLine).setScore(score--);

        // Build a list of skill entries using icons.
        List<String> skillEntries = new ArrayList<>();
        skillEntries.add(ChatColor.GREEN + "⚔ " + attackStats.getLevel());
        skillEntries.add(ChatColor.YELLOW + "🛡 " + defenceStats.getLevel());
        skillEntries.add(ChatColor.LIGHT_PURPLE + "💪 " + strengthStats.getLevel());
        skillEntries.add(ChatColor.GOLD + "🌳 " + woodStats.getLevel());
        skillEntries.add(ChatColor.RED + "🔥 " + fireStats.getLevel());
        skillEntries.add(ChatColor.DARK_AQUA + "⛏ " + miningStats.getLevel());
        skillEntries.add(ChatColor.BLUE + "🎣 " + fishingStats.getLevel());
        skillEntries.add(ChatColor.LIGHT_PURPLE + "🍳 " + cookingStats.getLevel());
        skillEntries.add(ChatColor.GRAY + "⚙ " + smeltingStats.getLevel());

        // Group skill entries into rows of 3.
        for (int i = 0; i < skillEntries.size(); i += 3) {
            StringBuilder row = new StringBuilder();
            row.append(skillEntries.get(i));
            if (i + 1 < skillEntries.size()) {
                row.append("    ").append(skillEntries.get(i + 1));
            }
            if (i + 2 < skillEntries.size()) {
                row.append("    ").append(skillEntries.get(i + 2));
            }
            objective.getScore(row.toString() + getInvisible(uniqueCounter++)).setScore(score--);
        }

        // Calculate total non-combat skill points (sum of Woodcutting, Firemaking, Mining, Fishing, Cooking, and Smelting).
        int totalNonCombatPoints = woodStats.getLevel() + fireStats.getLevel() + miningStats.getLevel()
                + fishingStats.getLevel() + cookingStats.getLevel() + smeltingStats.getLevel();
        String totalSkillLine = centerText(ChatColor.GOLD + "Total: " + totalNonCombatPoints)
                + getInvisible(uniqueCounter++);
        objective.getScore(totalSkillLine).setScore(score--);

        // Update the player's scoreboard.
        player.setScoreboard(scoreboard);
    }

    /**
     * Generates a string of invisible characters (using Minecraft's color code character)
     * to ensure each scoreboard entry is unique.
     *
     * @param count The number of invisible characters.
     * @return The generated string.
     */
    private String getInvisible(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ChatColor.COLOR_CHAR).append("r");
        }
        return sb.toString();
    }

    /**
     * Centers the provided text by adding padding (simple approximation).
     *
     * @param text The text to center.
     * @return The centered text.
     */
    private String centerText(String text) {
        int targetWidth = 30; // Adjust as needed.
        String plainText = text.replaceAll("§[0-9A-FK-OR]", "");
        int textLength = plainText.length();
        int spacesToAdd = (targetWidth - textLength) / 2;
        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < spacesToAdd; i++) {
            padding.append(" ");
        }
        return padding.toString() + text;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}