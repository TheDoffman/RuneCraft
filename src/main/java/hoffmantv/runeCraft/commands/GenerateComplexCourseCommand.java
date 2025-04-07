package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.agility.ComplexAgilityCourse;
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
        Player player = (Player) sender;
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection().normalize();
        // Place the course 10 blocks in front of the player.
        Location courseOrigin = playerLoc.clone().add(direction.multiply(10));
        // Set the Y-level to the player's current Y.
        courseOrigin.setY(playerLoc.getY());

        // Create a new complex course and generate it.
        ComplexAgilityCourse course = new ComplexAgilityCourse("Player Course");
        course.generateCourse(courseOrigin);

        player.sendMessage(ChatColor.GREEN + "Agility course generated in front of you at " + courseOrigin);
        return true;
    }
}