package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ScoreboardToggleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (RuneCraft.getInstance().isScoreboardEnabled(uuid)) {
            // Disable scoreboard by assigning a new, empty scoreboard.
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            RuneCraft.getInstance().setScoreboardEnabled(uuid, false);
            RuneCraft.getInstance().removeLeaderboard(uuid);
            player.sendMessage(ChatColor.YELLOW + "Scoreboard disabled.");
        } else {
            // Enable scoreboard.
            RuneCraft.getInstance().setScoreboardEnabled(uuid, true);
            player.sendMessage(ChatColor.GREEN + "Scoreboard enabled.");
        }
        return true;
    }
}