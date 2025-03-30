package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.mobs.MobSpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;

public class SetMobSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only allow players to run this command.
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can set mob spawn locations.");
            return true;
        }
        Player player = (Player) sender;
        Location loc = player.getLocation();

        // Determine mob type from command arguments; default to ZOMBIE if none provided.
        EntityType mobType = EntityType.ZOMBIE;
        if (args.length > 0) {
            try {
                mobType = EntityType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid mob type provided. Defaulting to ZOMBIE.");
            }
        }

        // Spawn the mob immediately at the player's location.
        player.getWorld().spawnEntity(loc, mobType);

        // Save the spawn location to MobSpawnManager so it can be respawned when it dies.
        MobSpawnManager.addMobSpawn(mobType, loc);

        player.sendMessage(ChatColor.GREEN + "Mob spawn for " + mobType.name() + " has been set at your current location.");
        return true;
    }
}