// CombatCommand.java
package hoffmantv.runeCraft.skills.combat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CombatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        // TODO: Implement combat mechanics such as targeting and damage calculation.
        player.sendMessage("Combat command executed. Prepare for battle!");
        return true;
    }
}