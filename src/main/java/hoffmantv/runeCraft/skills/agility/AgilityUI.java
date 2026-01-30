// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityUI.java
package hoffmantv.runeCraft.skills.agility;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class AgilityUI {
    public static void levelUpToast(Player p, int newLevel) {
        p.sendTitle(ChatColor.AQUA + "Agility " + ChatColor.GOLD + newLevel,
                ChatColor.GRAY + "You feel lighter on your feet!", 10, 60, 10);
        p.getWorld().spawnParticle(Particle.EXPLOSION, p.getLocation().add(0,1,0), 40, 0.5, 0.5, 0.5, 0.1);
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1.2f);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.6f);
    }
}