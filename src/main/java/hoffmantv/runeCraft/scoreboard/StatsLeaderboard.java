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
import hoffmantv.runeCraft.skills.agility.AgilityStats;
import hoffmantv.runeCraft.skills.agility.AgilityStatsManager;
import hoffmantv.runeCraft.skills.smithing.SmithingStats;
import hoffmantv.runeCraft.skills.smithing.SmithingStatsManager;
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
        AgilityStats agilityStats = AgilityStatsManager.getStats(player);
        SmithingStats smithingStats = SmithingStatsManager.getStats(player);

        // Ensure all skills are loaded.
        if (attackStats == null || defenceStats == null || strengthStats == null ||
                woodStats == null || fireStats == null || miningStats == null ||
                fishingStats == null || cookingStats == null || smeltingStats == null ||
                agilityStats == null || smithingStats == null) {
            return;
        }

        // Optionally, display the player's name.
        // String nameLine = ChatColor.AQUA + player.getName() + getInvisible(uniqueCounter++);
        // objective.getScore(nameLine).setScore(score--);

        // Display Combat Rank on a separate line.
        String combatRankLine = ChatColor.GRAY + "Combat: " + ChatColor.YELLOW + CombatLevelCalculator.getCombatLevel(player)
                + getInvisible(uniqueCounter++);
        objective.getScore(combatRankLine).setScore(score--);

        // Add a spacer.
        objective.getScore(ChatColor.DARK_GRAY + " " + getInvisible(uniqueCounter++)).setScore(score--);

        // Build a list of skill entries using icons in OSRS-style columns.
        List<String> skillEntries = new ArrayList<>();
        skillEntries.add(formatSkillCompact("⚔", attackStats.getLevel()));
        skillEntries.add(formatSkillCompact("🛡", defenceStats.getLevel()));
        skillEntries.add(formatSkillCompact("💪", strengthStats.getLevel()));
        skillEntries.add(formatSkillCompact("🏃", agilityStats.getLevel()));
        skillEntries.add(formatSkillCompact("🌳", woodStats.getLevel()));
        skillEntries.add(formatSkillCompact("🔥", fireStats.getLevel()));
        skillEntries.add(formatSkillCompact("⛏", miningStats.getLevel()));
        skillEntries.add(formatSkillCompact("🎣", fishingStats.getLevel()));
        skillEntries.add(formatSkillCompact("🍳", cookingStats.getLevel()));
        skillEntries.add(formatSkillCompact("⚙", smeltingStats.getLevel()));
        skillEntries.add(formatSkillCompact("⚒", smithingStats.getLevel()));

        // Group skill entries into rows of 3, similar to OSRS layout.
        for (int i = 0; i < skillEntries.size(); i += 3) {
            StringBuilder row = new StringBuilder();
            row.append(skillEntries.get(i));
            if (i + 1 < skillEntries.size()) {
                row.append("   ").append(skillEntries.get(i + 1));
            }
            if (i + 2 < skillEntries.size()) {
                row.append("   ").append(skillEntries.get(i + 2));
            }
            objective.getScore(row.toString() + getInvisible(uniqueCounter++)).setScore(score--);
        }

        // Total level (OSRS-style).
        int totalLevel = attackStats.getLevel() + defenceStats.getLevel() + strengthStats.getLevel()
                + agilityStats.getLevel() + woodStats.getLevel() + fireStats.getLevel()
                + miningStats.getLevel() + fishingStats.getLevel() + cookingStats.getLevel()
                + smeltingStats.getLevel() + smithingStats.getLevel();
        String totalSkillLine = centerText(ChatColor.GOLD + "Total Level: " + totalLevel)
                + getInvisible(uniqueCounter++);
        objective.getScore(totalSkillLine).setScore(score--);

        // Add a footer for visual separation.
        String footer = ChatColor.DARK_GRAY + "────────────";
        objective.getScore(footer + getInvisible(100)).setScore(0);

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

    private String formatSkillCompact(String icon, int level) {
        return ChatColor.GRAY + icon + " " + ChatColor.YELLOW + padLeft(level, 2);
    }

    private String padLeft(int value, int targetLength) {
        String text = String.valueOf(value);
        StringBuilder sb = new StringBuilder();
        while (sb.length() + text.length() < targetLength) {
            sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}