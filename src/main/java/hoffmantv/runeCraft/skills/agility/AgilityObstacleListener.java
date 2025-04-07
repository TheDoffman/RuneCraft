package hoffmantv.runeCraft.skills.agility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class AgilityObstacleListener implements Listener {

    // List of agility courses.
    private final List<ComplexAgilityCourse> courses = new ArrayList<>();

    // Tracks which obstacles each player has triggered.
    private final Map<UUID, Set<String>> playerTriggeredObstacles = new HashMap<>();

    public AgilityObstacleListener() {
        // Initialize a sample course with relative coordinates.
        ComplexAgilityCourse sampleCourse = new ComplexAgilityCourse("Sample Course");
        // Define obstacles relative to the course origin.
        // For example, "Jump Gap" from (0,0,0) to (5,6,5)
        sampleCourse.addObstacle(new Obstacle("Jump Gap", 0, 0, 0, 5, 6, 5, 10.0));
        // "Wall Climb" from (7,0,0) to (12,6,3)
        sampleCourse.addObstacle(new Obstacle("Wall Climb", 7, 0, 0, 12, 6, 3, 15.0));
        courses.add(sampleCourse);

        // Automatically generate the sample course at a fixed location.
        Location courseLocation = new Location(Bukkit.getWorld("world"), 100, 64, 100);
        sampleCourse.generateCourse(courseLocation, Material.WHITE_CONCRETE);
        Bukkit.getLogger().info("Agility course 'Sample Course' generated at " + courseLocation);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Set<String> triggered = playerTriggeredObstacles.computeIfAbsent(uuid, k -> new HashSet<>());

        // Check each course and each obstacle.
        for (AgilityCourse course : courses) {
            for (Obstacle obstacle : course.getObstacles()) {
                // Convert player's location to relative coordinates based on the course's origin.
                // For simplicity, assume the course origin is (100,64,100) (as used above).
                Location origin = new Location(Bukkit.getWorld("world"), 100, 64, 100);
                // Calculate relative location.
                double relX = player.getLocation().getX() - origin.getX();
                double relY = player.getLocation().getY() - origin.getY();
                double relZ = player.getLocation().getZ() - origin.getZ();
                Location relLocation = new Location(null, relX, relY, relZ);

                if (!triggered.contains(obstacle.getObstacleName()) && obstacle.contains(relLocation)) {
                    AgilityStats stats = AgilityStatsManager.getStats(player);
                    if (stats != null) {
                        stats.addExperience(obstacle.getXpReward(), player);
                        player.sendMessage(ChatColor.GREEN + "You completed " + obstacle.getObstacleName() +
                                " and earned " + obstacle.getXpReward() + " XP!");
                    }
                    triggered.add(obstacle.getObstacleName());
                }
            }
        }
    }
}