// Updated TestLevelUpCommand.java – change line 33 to use getCombatLevel()
package hoffmantv.runeCraft.commands;

import hoffmantv.runeCraft.skills.CombatStats;
import hoffmantv.runeCraft.skills.PlayerCombatStatsManager;
import hoffmantv.runeCraft.skills.SkillRewardUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class TestLevelUpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }
        Player player = (Player) sender;

        // Retrieve the player's CombatStats.
        CombatStats stats = PlayerCombatStatsManager.getStats(player);

        // Simulate a kill that grants enough XP to level up the stats.
        stats.addKillExperience(1000, player);

        // Save the updated stats immediately.
        stats.save(player);
        hoffmantv.runeCraft.skills.PlayerSkillDataManager.saveData(hoffmantv.runeCraft.RuneCraft.getInstance());

        // Trigger the level-up effect for the Combat skill using overall combat level.
        SkillRewardUtils.triggerSkillRankUpEffect(player, "Combat", stats.getCombatLevel());

        player.sendMessage(ChatColor.GREEN + "Test level up executed. Check your screen for the level up effect!");
        return true;
    }
}