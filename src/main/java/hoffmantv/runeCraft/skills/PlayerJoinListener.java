package hoffmantv.runeCraft.skills;

import hoffmantv.runeCraft.skills.attack.AttackStatsManager;
import hoffmantv.runeCraft.skills.cooking.CookingStatsManager;
import hoffmantv.runeCraft.skills.defence.DefenceStatsManager;
import hoffmantv.runeCraft.skills.firemaking.FiremakingStatsManager;
import hoffmantv.runeCraft.skills.fishing.FishingStatsManager;
import hoffmantv.runeCraft.skills.mining.MiningStatsManager;
import hoffmantv.runeCraft.skills.smelting.SmeltingStatsManager;
import hoffmantv.runeCraft.skills.strength.StrengthStatsManager;
import hoffmantv.runeCraft.skills.woodcutting.WoodcuttingStatsManager;
import hoffmantv.runeCraft.skills.agility.AgilityStatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// PlayerJoinListener.java
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        WoodcuttingStatsManager.loadPlayer(player);
        FiremakingStatsManager.loadPlayer(player);
        MiningStatsManager.loadPlayer(player);
        FishingStatsManager.loadPlayer(player);
        CookingStatsManager.loadPlayer(player);
        SmeltingStatsManager.loadPlayer(player);
        AttackStatsManager.loadPlayer(player);
        StrengthStatsManager.loadPlayer(player);
        DefenceStatsManager.loadPlayer(player);
        AgilityStatsManager.get(player);

    }
}
