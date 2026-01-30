package hoffmantv.runeCraft.skills.agility;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AgilityListener implements Listener {
    private static final Map<UUID, String> ACTIVE_COURSE = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> PROGRESS = new ConcurrentHashMap<>();

    public static void startCourse(Player p, String name) {
        ACTIVE_COURSE.put(p.getUniqueId(), name);
        PROGRESS.put(p.getUniqueId(), 0);
        p.sendMessage(ChatColor.AQUA + "Started agility course: " + ChatColor.GOLD + name);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo() == null) return;
        Player p = e.getPlayer();
        String course = ACTIVE_COURSE.get(p.getUniqueId());
        if (course == null) return;

        List<AgilityCourseManager.CourseNode> nodes = AgilityCourseManager.getCourse(course);
        int idx = PROGRESS.getOrDefault(p.getUniqueId(), 0);
        if (idx >= nodes.size()) {
            // Completed
            p.sendMessage(ChatColor.GREEN + "Course complete!");
            ACTIVE_COURSE.remove(p.getUniqueId());
            PROGRESS.remove(p.getUniqueId());
            return;
        }

        AgilityCourseManager.CourseNode node = nodes.get(idx);
        if (isOnNode(p.getLocation(), node.loc())) {
            // Level check
            AgilityStats stats = AgilityStatsManager.get(p);
            if (stats.getLevel() < node.levelReq()) {
                p.sendMessage(ChatColor.RED + "Agility " + node.levelReq() + " required for this obstacle.");
                return;
            }
            // Award XP and progress
            stats.addExperience(node.xp(), p);
            stats.save(p);
            p.spawnParticle(Particle.END_ROD, p.getLocation().add(0,1,0), 8, 0.2,0.2,0.2,0.01);
            p.sendMessage(ChatColor.GRAY + "Obstacle " + (idx+1) + "/" + nodes.size() + " cleared. +" + node.xp() + " xp");
            PROGRESS.put(p.getUniqueId(), idx + 1);
        }
    }

    private boolean isOnNode(Location loc, Location node) {
        return loc.getWorld() == node.getWorld()
                && loc.getBlockX() == node.getBlockX()
                && Math.abs(loc.getY() - node.getY()) < 1.2
                && loc.getBlockZ() == node.getBlockZ();
    }
}