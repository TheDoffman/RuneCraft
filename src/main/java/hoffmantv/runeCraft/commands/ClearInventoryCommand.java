package hoffmantv.runeCraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player.
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }
        if (!sender.hasPermission("rc.clearinventory")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to clear inventory.");
            return true;
        }

        Player player = (Player) sender;
        // Clear the player's inventory.
        player.getInventory().clear();
        player.sendMessage(ChatColor.GREEN + "Your inventory has been cleared.");
        return true;
    }
}