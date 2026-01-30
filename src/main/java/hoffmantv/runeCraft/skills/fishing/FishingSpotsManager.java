package hoffmantv.runeCraft.skills.fishing;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.config.YamlConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;
import java.util.ArrayList;
import java.util.List;

public class FishingSpotsManager {

    private static YamlConfigManager configManager;
    private static final List<Location> fishingSpots = new ArrayList<>();
    private static BukkitTask effectTask;

    public static void init(RuneCraft plugin) {
        configManager = new YamlConfigManager(plugin, "fishingSpots.yml");
        configManager.init();
        loadFishingSpots();
        startEffectTask();
    }

    private static void loadFishingSpots() {
        fishingSpots.clear();
        if (configManager.getConfig().isConfigurationSection("spots")) {
            for (String key : configManager.getConfig().getConfigurationSection("spots").getKeys(false)) {
                double x = configManager.getConfig().getDouble("spots." + key + ".x");
                double y = configManager.getConfig().getDouble("spots." + key + ".y");
                double z = configManager.getConfig().getDouble("spots." + key + ".z");
                String worldName = configManager.getConfig().getString("spots." + key + ".world");
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    Location loc = new Location(world, x, y, z);
                    fishingSpots.add(loc);
                }
            }
        }
    }

    public static List<Location> getFishingSpots() {
        return fishingSpots;
    }

    /**
     * Returns true if the provided location is within 3 blocks (horizontally)
     * of any designated fishing spot.
     */
    public static boolean isFishingSpot(Location loc) {
        for (Location spot : fishingSpots) {
            if (spot.getWorld() != null && loc.getWorld() != null && spot.getWorld().equals(loc.getWorld())) {
                int dx = spot.getBlockX() - loc.getBlockX();
                int dz = spot.getBlockZ() - loc.getBlockZ();
                if ((dx * dx + dz * dz) <= 9) { // 3 blocks radius squared (9)
                    return true;
                }
            }
        }
        return false;
    }

    public static void addFishingSpot(Location loc) {
        fishingSpots.add(loc);
        int index = fishingSpots.size();
        configManager.getConfig().set("spots.spot" + index + ".x", loc.getX());
        configManager.getConfig().set("spots.spot" + index + ".y", loc.getY());
        configManager.getConfig().set("spots.spot" + index + ".z", loc.getZ());
        configManager.getConfig().set("spots.spot" + index + ".world", loc.getWorld().getName());
        saveConfig();

        // Play immediate effects to signal the spot is active.
        loc.getWorld().spawnParticle(Particle.SPLASH, loc.clone().add(0.5, 1, 0.5), 20, 0.5, 0.5, 0.5, 0.2);
        loc.getWorld().playSound(loc, Sound.BLOCK_WATER_AMBIENT, 1.0F, 1.0F);
    }

    private static void saveConfig() {
        if (configManager != null) {
            configManager.save();
        }
    }

    /**
     * Starts a repeating task that displays water splash particles at each fishing spot.
     */
    public static void startEffectTask() {
        if (effectTask != null) {
            effectTask.cancel();
        }
        effectTask = Bukkit.getScheduler().runTaskTimer(RuneCraft.getInstance(), () -> {
            for (Location spot : fishingSpots) {
                if (spot.getWorld() != null) {
                    spot.getWorld().spawnParticle(Particle.SPLASH, spot.clone().add(0.5, 1, 0.5),
                            10, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }, 0L, 40L);
    }

    public static void shutdown() {
        if (effectTask != null) {
            effectTask.cancel();
            effectTask = null;
        }
        if (configManager != null) {
            saveConfig();
        }
    }
}