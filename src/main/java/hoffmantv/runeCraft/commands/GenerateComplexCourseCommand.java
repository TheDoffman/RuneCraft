package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.skills.agility.AgilityProceduralGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GenerateComplexCourseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can run this command.
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can generate an agility course.");
            return true;
        }
        if (!sender.hasPermission("rc.generatecourse")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to generate courses.");
            return true;
        }
        Player player = (Player) sender;
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection().normalize();
        // Place the course 10 blocks in front of the player.
        Location courseOrigin = playerLoc.clone().add(direction.multiply(10));
        // Set the Y level to the player's current Y.
        courseOrigin.setY(playerLoc.getY());

        String courseName = "Player Course";
        int radius = 10;
        int difficulty = 3;
        AgilityProceduralGenerator.generate(player, courseName, radius, difficulty);

        player.sendMessage(ChatColor.GREEN + "Agility course generated in front of you at " + courseOrigin);
        return true;
    }
}