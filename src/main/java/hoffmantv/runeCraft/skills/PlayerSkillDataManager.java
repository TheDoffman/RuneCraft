// File: PlayerSkillDataManager.java
package hoffmantv.runeCraft.skills;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Manages a separate YAML file ("playerskills.yml") to store player skill experience and rank data.
 * This file can be accessed by all current and future skills.
 */
public class PlayerSkillDataManager {

    private static File playerDataFile;
    private static FileConfiguration playerDataConfig;

    /**
     * Sets up the playerskills.yml file.
     * Should be called in the onEnable() method of your main plugin class.
     *
     * @param plugin Your main JavaPlugin instance.
     */
    public static void setup(JavaPlugin plugin) {
        // Ensure the plugin folder exists.
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Create or load the playerskills.yml file.
        playerDataFile = new File(plugin.getDataFolder(), "playerskills.yml");
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create playerskills.yml");
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    /**
     * Returns the FileConfiguration representing playerskills.yml.
     *
     * @return The FileConfiguration.
     */
    public static FileConfiguration getData() {
        return playerDataConfig;
    }

    /**
     * Saves the current state of playerskills.yml to disk.
     *
     * @param plugin Your main JavaPlugin instance.
     */
    public static void saveData(JavaPlugin plugin) {
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save playerskills.yml");
            e.printStackTrace();
        }
    }

    /**
     * Reloads the playerskills.yml file from disk.
     */
    public static void reloadData() {
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    // Example methods to access and modify a player's skill XP and rank.
    // Player data is stored by their UUID. Each player has a "skills" section
    // that can hold multiple skills (e.g., "combat", "mining", "fishing", etc.)

    /**
     * Sets the XP for a given skill for a player.
     *
     * @param uuid  The player's UUID as a string.
     * @param skill The name of the skill.
     * @param xp    The experience points to set.
     */
    public static void setPlayerSkillXP(String uuid, String skill, double xp) {
        String path = uuid + ".skills." + skill + ".xp";
        playerDataConfig.set(path, xp);
    }

    /**
     * Gets the XP for a given skill for a player.
     *
     * @param uuid  The player's UUID as a string.
     * @param skill The name of the skill.
     * @return The current experience points for that skill (default 0).
     */
    public static double getPlayerSkillXP(String uuid, String skill) {
        String path = uuid + ".skills." + skill + ".xp";
        return playerDataConfig.getDouble(path, 0);
    }

    /**
     * Sets the rank for a given skill for a player.
     *
     * @param uuid  The player's UUID as a string.
     * @param skill The name of the skill.
     * @param rank  The rank to set.
     */
    public static void setPlayerSkillRank(String uuid, String skill, int rank) {
        String path = uuid + ".skills." + skill + ".rank";
        playerDataConfig.set(path, rank);
    }

    /**
     * Gets the rank for a given skill for a player.
     *
     * @param uuid  The player's UUID as a string.
     * @param skill The name of the skill.
     * @return The player's rank for that skill (default 0).
     */
    public static int getPlayerSkillRank(String uuid, String skill) {
        String path = uuid + ".skills." + skill + ".rank";
        return playerDataConfig.getInt(path, 0);
    }
}