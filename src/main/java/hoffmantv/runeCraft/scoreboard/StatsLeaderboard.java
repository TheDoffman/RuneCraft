package hoffmantv.runeCraft.scoreboard;

import hoffmantv.runeCraft.skills.CombatStats;
import hoffmantv.runeCraft.skills.PlayerCombatStatsManager;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStats;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStatsManager;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStats;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
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
        // Clear existing entries.
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // Footer line (can be used for a global message).
        String footer = ChatColor.GOLD + "————————————";
        objective.getScore(footer + getInvisible(100)).setScore(0);

        // Get a sorted list of online players, sorted by overall combat rank (descending).
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.sort(Comparator.comparingInt(p -> -getCombatRank(p)));

        int score = 99;
        for (Player player : players) {
            CombatStats combatStats = PlayerCombatStatsManager.getStats(player);
            WoodcuttingStats woodStats = WoodcuttingStatsManager.getStats(player);
            FiremakingStats fireStats = FiremakingStatsManager.getStats(player);
            if (combatStats == null || woodStats == null || fireStats == null) continue;

            // Compute total of all skill ranks.
            int total = combatStats.getAttackLevel() + combatStats.getDefenceLevel() +
                    combatStats.getStrengthLevel() + woodStats.getLevel() + fireStats.getLevel();

            // Line 1: Player name.
            String line1 = ChatColor.AQUA + player.getName() + getInvisible(1);
            objective.getScore(line1).setScore(score--);

            // Line 2: Row 1 of skills: Combat Rank, Attack, Defence.
            // Here, Combat Rank is taken as CombatStats.getCombatLevel().
            String line2 = ChatColor.GRAY + "C:" + combatStats.getCombatLevel() + "   " +
                    ChatColor.GREEN + "⚔:" + combatStats.getAttackLevel() + "   " +
                    ChatColor.YELLOW + "🛡:" + combatStats.getDefenceLevel() + getInvisible(2);
            objective.getScore(line2).setScore(score--);

            // Line 3: Row 2 of skills: Strength, Woodcutting, Firemaking.
            String line3 = ChatColor.LIGHT_PURPLE + "💪:" + combatStats.getStrengthLevel() + "   " +
                    ChatColor.GOLD + "🌳:" + woodStats.getLevel() + "   " +
                    ChatColor.RED + "🔥:" + fireStats.getLevel() + getInvisible(3);
            objective.getScore(line3).setScore(score--);

            // Line 4: Total skill ranks.
            String line4 = ChatColor.WHITE + "Total: " + total + getInvisible(4);
            objective.getScore(line4).setScore(score--);

            // Blank separator.
            if (score > 0) {
                String blank = ChatColor.RESET + " " + getInvisible(5);
                objective.getScore(blank).setScore(score--);
            }
        }

        // Set the scoreboard for every online player.
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    // Helper method to generate a string of invisible characters to ensure uniqueness.
    private String getInvisible(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ChatColor.COLOR_CHAR).append("r");
        }
        return sb.toString();
    }

    // Helper method to get a player's combat rank.
    private int getCombatRank(Player player) {
        CombatStats stats = PlayerCombatStatsManager.getStats(player);
        return stats != null ? stats.getCombatLevel() : 0;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}