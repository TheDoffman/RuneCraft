package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.agility.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AgilityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (!(s instanceof Player p)) {
            s.sendMessage("Players only.");
            return true;
        }
        if (!s.hasPermission("rc.agility")) {
            s.sendMessage(ChatColor.RED + "You don't have permission to use agility commands.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.AQUA + "/agility start <course>");
            p.sendMessage(ChatColor.AQUA + "/agility gen <course> <radius> <difficulty(1-5)>");
            p.sendMessage(ChatColor.AQUA + "/agility level");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (args.length < 2) { p.sendMessage(ChatColor.RED + "Usage: /agility start <course>"); return true; }
                AgilityListener.startCourse(p, args[1]);
            }
            case "gen" -> {
                if (args.length < 4) { p.sendMessage(ChatColor.RED + "Usage: /agility gen <course> <radius> <difficulty>"); return true; }
                String name = args[1];
                int radius;
                int diff;
                try {
                    radius = Math.max(5, Integer.parseInt(args[2]));
                    diff = Math.min(5, Math.max(1, Integer.parseInt(args[3])));
                } catch (NumberFormatException ex) {
                    p.sendMessage(ChatColor.RED + "Radius and difficulty must be numbers.");
                    return true;
                }
                AgilityProceduralGenerator.generate(p, name, radius, diff);
            }
            case "level" -> {
                AgilityStats stats = AgilityStatsManager.get(p);
                p.sendMessage(ChatColor.GOLD + "Agility Level: " + stats.getLevel() + ChatColor.GRAY + " (" + (int)stats.getXp() + " xp)");
                p.sendMessage(ChatColor.GRAY + "Next lvl at: " + (int)AgilityXP.levelToXp(stats.getLevel()+1));
            }
            default -> p.sendMessage(ChatColor.RED + "Unknown subcommand.");
        }

        return true;
    }

    public static void register(RuneCraft plugin) {
        plugin.getCommand("agility").setExecutor(new AgilityCommand());
    }
}