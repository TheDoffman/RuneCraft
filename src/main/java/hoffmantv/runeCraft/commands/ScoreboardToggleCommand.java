package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.scoreboard.StatsLeaderboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ScoreboardToggleCommand implements CommandExecutor {

    // Tracks which players have the scoreboard enabled.
    private static final Set<UUID> scoreboardEnabled = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (scoreboardEnabled.contains(uuid)) {
            // Disable scoreboard by assigning a new, empty scoreboard.
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            scoreboardEnabled.remove(uuid);
            player.sendMessage(ChatColor.YELLOW + "Scoreboard disabled.");
        } else {
            // Enable scoreboard.
            StatsLeaderboard leaderboard = new StatsLeaderboard();
            leaderboard.update(player);
            player.setScoreboard(leaderboard.getScoreboard());
            scoreboardEnabled.add(uuid);
            player.sendMessage(ChatColor.GREEN + "Scoreboard enabled.");
        }
        return true;
    }
}