package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.skills.fishing.FishingSpotsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetFishingSpotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can set a fishing spot.");
            return true;
        }
        if (!sender.hasPermission("rc.fishingspotset")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to set fishing spots.");
            return true;
        }
        Player player = (Player) sender;
        FishingSpotsManager.addFishingSpot(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Fishing spot added at your current location.");
        return true;
    }
}