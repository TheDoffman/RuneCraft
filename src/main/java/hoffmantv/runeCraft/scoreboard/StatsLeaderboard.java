package hoffmantv.runeCraft.scoreboard;

import hoffmantv.runeCraft.skills.combat.CombatStats;
import hoffmantv.runeCraft.skills.combat.PlayerCombatStatsManager;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStats;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStatsManager;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStats;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
import hoffmantv.runeCraft.skills.mining.MiningStats;
import hoffmantv.runeCraft.skills.mining.MiningStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatsLeaderboard {
    private final Scoreboard scoreboard;
    private final Objective objective;

    public StatsLeaderboard() {
        // Create a new scoreboard and register a dummy objective for the sidebar.
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        // Use the display name as the header.
        objective = scoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "✦ Player Stats ✦");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void update() {
        // Clear existing entries.
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // Add a footer for visual separation.
        String footer = ChatColor.GOLD + "————————————";
        objective.getScore(footer + getInvisible(100)).setScore(0);

        // Get a list of online players sorted by combat rank (descending).
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.sort(Comparator.comparingInt(p -> -getCombatRank(p)));

        int score = 99;
        int uniqueCounter = 0;
        for (Player player : players) {
            CombatStats combatStats = PlayerCombatStatsManager.getStats(player);
            WoodcuttingStats woodStats = WoodcuttingStatsManager.getStats(player);
            FiremakingStats fireStats = FiremakingStatsManager.getStats(player);
            MiningStats miningStats = MiningStatsManager.getStats(player);
            if (combatStats == null || woodStats == null || fireStats == null || miningStats == null) continue;

            // First line: Player's name.
            String line1 = ChatColor.AQUA + player.getName() + getInvisible(uniqueCounter++);
            objective.getScore(line1).setScore(score--);

            // Second line: Combat Rank.
            String line2 = ChatColor.GRAY + "C:" + combatStats.getCombatLevel() + getInvisible(uniqueCounter++);
            objective.getScore(line2).setScore(score--);

            // Build individual skill strings.
            String attack = ChatColor.GREEN + "⚔ " + combatStats.getAttackLevel();
            String defence = ChatColor.YELLOW + "🛡 " + combatStats.getDefenceLevel();
            String strength = ChatColor.LIGHT_PURPLE + "💪 " + combatStats.getStrengthLevel();
            String woodcutting = ChatColor.GOLD + "🌳 " + woodStats.getLevel();
            String firemaking = ChatColor.RED + "🔥 " + fireStats.getLevel();
            String mining = ChatColor.DARK_AQUA + "⚒ " + miningStats.getLevel();

            // Add these to a list.
            List<String> skills = new ArrayList<>();
            skills.add(attack);
            skills.add(defence);
            skills.add(strength);
            skills.add(woodcutting);
            skills.add(firemaking);
            skills.add(mining);

            // Group the skills into rows of 3.
            List<String> rows = new ArrayList<>();
            for (int i = 0; i < skills.size(); i += 3) {
                StringBuilder row = new StringBuilder();
                row.append(skills.get(i));
                if (i + 1 < skills.size()) {
                    row.append("    ").append(skills.get(i + 1));
                }
                if (i + 2 < skills.size()) {
                    row.append("    ").append(skills.get(i + 2));
                }
                rows.add(row.toString() + getInvisible(uniqueCounter++));
            }
            // Add each row to the scoreboard.
            for (String row : rows) {
                objective.getScore(row).setScore(score--);
            }

            // Optionally, add a blank separator line between players.
            if (score > 0) {
                objective.getScore(ChatColor.RESET + " " + getInvisible(uniqueCounter++)).setScore(score--);
            }
        }

        // Update each player's scoreboard.
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    // Helper method to generate a string of invisible characters for uniqueness.
    private String getInvisible(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ChatColor.COLOR_CHAR).append("r");
        }
        return sb.toString();
    }

    // Helper method: get player's combat rank.
    private int getCombatRank(Player player) {
        CombatStats stats = PlayerCombatStatsManager.getStats(player);
        return stats != null ? stats.getCombatLevel() : 0;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}