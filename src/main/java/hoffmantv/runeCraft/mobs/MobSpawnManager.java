package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobSpawnManager {

    private static File configFile;
    private static FileConfiguration config;
    // Map of mob type to a list of spawn locations.
    private static final Map<EntityType, List<Location>> mobSpawnMap = new HashMap<>();

    public static void init(RuneCraft plugin) {
        configFile = new File(plugin.getDataFolder(), "mobSpawns.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadMobSpawns();
    }

    private static void loadMobSpawns() {
        mobSpawnMap.clear();
        if (config.isConfigurationSection("spawns")) {
            for (String typeStr : config.getConfigurationSection("spawns").getKeys(false)) {
                try {
                    EntityType mobType = EntityType.valueOf(typeStr);
                    List<Location> locations = new ArrayList<>();
                    if (config.isConfigurationSection("spawns." + typeStr)) {
                        for (String key : config.getConfigurationSection("spawns." + typeStr).getKeys(false)) {
                            double x = config.getDouble("spawns." + typeStr + "." + key + ".x");
                            double y = config.getDouble("spawns." + typeStr + "." + key + ".y");
                            double z = config.getDouble("spawns." + typeStr + "." + key + ".z");
                            String worldName = config.getString("spawns." + typeStr + "." + key + ".world");
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
        List<Location> locations = mobSpawnMap.getOrDefault(mobType, new ArrayList<>());
        locations.add(loc);
        mobSpawnMap.put(mobType, locations);
        int index = locations.size();
        config.set("spawns." + mobType.name() + ".spawn" + index + ".x", loc.getX());
        config.set("spawns." + mobType.name() + ".spawn" + index + ".y", loc.getY());
        config.set("spawns." + mobType.name() + ".spawn" + index + ".z", loc.getZ());
        config.set("spawns." + mobType.name() + ".spawn" + index + ".world", loc.getWorld().getName());
        saveConfig();
    }

    private static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
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