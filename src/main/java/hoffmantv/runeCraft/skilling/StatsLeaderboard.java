package hoffmantv.runeCraft.skilling;

import hoffmantv.runeCraft.skilling.woodcutting.WoodcuttingStats;
import hoffmantv.runeCraft.skilling.woodcutting.WoodcuttingStatsManager;
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
        objective = scoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "✦ Player Stats ✦");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void update() {
        // Clear any existing entries.
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // Footer entry.
        String footer = ChatColor.GOLD + "==================";
        objective.getScore(footer).setScore(0);

        // Get a sorted list of online players (sorted by overall combat level descending).
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.sort(Comparator.comparingInt(p -> -PlayerCombatStatsManager.getStats(p).getCombatLevel()));

        int score = 99;
        for (Player player : players) {
            CombatStats combatStats = PlayerCombatStatsManager.getStats(player);
            WoodcuttingStats woodStats = WoodcuttingStatsManager.getStats(player);
            if (combatStats == null || woodStats == null) continue;

            // First line: player name with overall combat level.
            String line1 = ChatColor.AQUA + player.getName() + ChatColor.GRAY + " [C:" + combatStats.getCombatLevel() + "]";
            objective.getScore(line1).setScore(score--);

            // Second line: Display skills.
            // Using icons: ⚔ for Attack, 🛡 for Defence, 💪 for Strength, and 🌳 for Woodcutting.
            String line2 = ChatColor.DARK_GRAY + " " +
                    "⚔ " + ChatColor.GREEN + combatStats.getAttackLevel() + "    " +
                    "🛡 " + ChatColor.YELLOW + combatStats.getDefenceLevel() + "    " +
                    "💪 " + ChatColor.LIGHT_PURPLE + combatStats.getStrengthLevel() + "    " +
                    "🌳 " + ChatColor.GOLD + woodStats.getLevel();
            objective.getScore(line2).setScore(score--);

            // Optionally, add a blank separator line.
            if (score > 0) {
                String blank = ChatColor.RESET + " ";
                objective.getScore(blank).setScore(score--);
            }
        }

        // Update every player's scoreboard.
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}