package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.config.YamlConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobSpawnManager {

    private static YamlConfigManager configManager;
    // Map of mob type to a list of spawn locations.
    private static final Map<EntityType, List<Location>> mobSpawnMap = new HashMap<>();

    public static void init(RuneCraft plugin) {
        configManager = new YamlConfigManager(plugin, "mobSpawns.yml");
        configManager.init();
        loadMobSpawns();
    }

    private static void loadMobSpawns() {
        mobSpawnMap.clear();
        if (configManager.getConfig().isConfigurationSection("spawns")) {
            for (String typeStr : configManager.getConfig().getConfigurationSection("spawns").getKeys(false)) {
                try {
                    EntityType mobType = EntityType.valueOf(typeStr);
                    List<Location> locations = new ArrayList<>();
                    if (configManager.getConfig().isConfigurationSection("spawns." + typeStr)) {
                        for (String key : configManager.getConfig().getConfigurationSection("spawns." + typeStr).getKeys(false)) {
                            double x = configManager.getConfig().getDouble("spawns." + typeStr + "." + key + ".x");
                            double y = configManager.getConfig().getDouble("spawns." + typeStr + "." + key + ".y");
                            double z = configManager.getConfig().getDouble("spawns." + typeStr + "." + key + ".z");
                            String worldName = configManager.getConfig().getString("spawns." + typeStr + "." + key + ".world");
                            if (worldName == null) {
                                continue;
                            }
                            World world = Bukkit.getWorld(worldName);
                            if (world != null) {
                                locations.add(new Location(world, x, y, z));
                            }
                        }
                    }
                    mobSpawnMap.put(mobType, locations);
                } catch (IllegalArgumentException ex) {
                    Bukkit.getLogger().warning("Invalid mob type in mobSpawns.yml: " + typeStr);
                }
            }
        }
    }

    public static Map<EntityType, List<Location>> getMobSpawnMap() {
        return mobSpawnMap;
    }

    /**
     * Adds a spawn location for the specified mob type.
     *
     * @param mobType The type of mob.
     * @param loc     The location where the mob should spawn.
     */
    public static void addMobSpawn(EntityType mobType, Location loc) {
        if (loc.getWorld() == null) {
            Bukkit.getLogger().warning("Cannot add mob spawn without a world for " + mobType);
            return;
        }
        List<Location> locations = mobSpawnMap.getOrDefault(mobType, new ArrayList<>());
        locations.add(loc);
        mobSpawnMap.put(mobType, locations);
        int index = locations.size();
        configManager.getConfig().set("spawns." + mobType.name() + ".spawn" + index + ".x", loc.getX());
        configManager.getConfig().set("spawns." + mobType.name() + ".spawn" + index + ".y", loc.getY());
        configManager.getConfig().set("spawns." + mobType.name() + ".spawn" + index + ".z", loc.getZ());
        configManager.getConfig().set("spawns." + mobType.name() + ".spawn" + index + ".world", loc.getWorld().getName());
        saveConfig();
    }

    private static void saveConfig() {
        if (configManager != null) {
            configManager.save();
        }
    }

    /**
     * Returns a list of spawn locations for the given mob type.
     *
     * @param mobType The type of mob.
     * @return A list of Locations where that mob should spawn.
     */
    public static List<Location> getMobSpawns(EntityType mobType) {
        return mobSpawnMap.getOrDefault(mobType, new ArrayList<>());
    }
}